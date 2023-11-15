package fyi.pauli.ichor.gaia.server

import fyi.pauli.ichor.gaia.config.ServerConfig
import fyi.pauli.ichor.gaia.config.loadConfig
import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.extensions.koin.KoinLogger
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import java.net.SocketException
import java.nio.file.Path
import java.security.KeyPair
import java.security.KeyPairGenerator
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.Path

/**
 * Start function for you server. Highly recommended to use as this is the only supported way to start the server correctly.
 * @param server instance of you server class.
 * @param init in this part you should register things you need already at startup like configurations.
 * @author Paul Kindler
 * @since 01/11/2023
 * @see Server
 */
suspend fun <S : Server> serve(server: S, init: S.() -> Unit = {}) = server.apply(init).internalStart()

/**
 * Super class of all server instances.
 * @property serverName name of the server. A.e: Hephaistos/Hermes
 * @author Paul Kindler
 * @since 14/11/2023
 */
abstract class Server(private val serverName: String) : CoroutineScope {

	/**
	 * Module for all configurations you need to have at runtime.
	 * You don't need to access this Module.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Module
	 */
	val configurationsModule = Module()

	/**
	 * Function to register a fileConfiguration at runtime.
	 * You only need to call this function one time in the init block. After you can just inject it.
	 */
	inline fun <reified C> config(path: Path, fileConfiguration: C) {
		configurationsModule.factory<C> { loadConfig<C>(path, fileConfiguration) }
	}

	/**
	 * Job used for the server.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Job
	 */
	private val job: Job = Job()

	/**
	 * Http client used for making requests to Mojang servers etc.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see HttpClient
	 */
	abstract val httpClient: HttpClient

	/**
	 * Encryption keypair used for authentication.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see KeyPair
	 */
	val encryptionPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
		initialize(1024)
	}.genKeyPair()

	/**
	 * Verify token, currently just example and test purposes.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	val verifyToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toByteArray()

	/**
	 * Collection of all current connections used for packet sending/receiving.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see PacketHandle
	 */
	private val handles: MutableSet<PacketHandle> = mutableSetOf()

	/**
	 * Collection of all currently connected players.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Player
	 */
	val players: MutableSet<Player> = mutableSetOf()

	/**
	 * Function to get the PacketHandle of a connection.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Connection
	 * @see PacketHandle
 	 */
	private fun Connection.handle() = PacketHandle(
		state = State.HANDSHAKING, connection = this, server = this@Server
	).also {
		handles.add(it)

		launch {
			this@handle.socket.awaitClosed()
			handles.removeIf { handle -> handle.connection.socket == this@handle.socket }
		}
	}

	/**
	 * Default logger of the server instance.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see KLogger
	 */
	val logger: KLogger
		get() = KotlinLogging.logger(serverName)

	/**
	 * Custom exception handler in coroutine contexts.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see CoroutineExceptionHandler
	 */
	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		logger.error(throwable) { throwable.stackTraceToString() }
	}

	/**
	 * Coroutine context of the server.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see CoroutineContext
	 */
	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job + coroutineExceptionHandler

	/**
	 * Function called at startup of the server. Startup logic should be executed here.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	abstract suspend fun startup()

	/**
	 * Function used to initiate internal logic like koin or connection management.
	 * Also executes [startup] when koin is started.
	 */
	internal suspend fun internalStart() = coroutineScope {
		val serverConfig: ServerConfig =
			loadConfig(Path(System.getenv()["SERVER_BASE_CONFIG"] ?: "./config.toml"), ServerConfig())
		configurationsModule.single { serverConfig }

		startKoin {
			logger(KoinLogger(logger = logger))
			modules(
				configurationsModule
			)
		}

		startup()

		val manager = SelectorManager(Dispatchers.IO)
		val serverSocket = aSocket(manager).tcp().bind(
			InetSocketAddress(serverConfig.server.host, serverConfig.server.port)
		)

		logger.info {
			"Server started successfully!"
		}

		while (!serverSocket.isClosed) {
			val socket = serverSocket.accept()

			val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel(true))

			val handle = connection.handle()

			logger.debug {
				"New connection (Socket: ${connection.socket.remoteAddress})"
			}

			launch {
				try {
					handle.handleIncoming()
				} catch (e: Throwable) {
					if (e !is ClosedReceiveChannelException && e !is SocketException) logger.error(e) {
						"Error in channel"
					}
				} finally {
					handles.remove(handle)
					connection.input.cancel()
					connection.output.close()
				}
			}
		}
	}

	/**
	 * Function called at shutdown of the server. Shutdown logic should be executed here.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	abstract suspend fun shutdown()

	/**
	 * Initiation logic of the server used to add shutdown hook and default exception handler.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	init {
		Runtime.getRuntime().addShutdownHook(Thread {
			runBlocking {
				shutdown()
				job.cancel()
			}
		})

		Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
			logger.error(throwable) { throwable.stackTraceToString() }
		}
	}
}
