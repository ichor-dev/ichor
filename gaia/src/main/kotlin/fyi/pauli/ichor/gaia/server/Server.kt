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

suspend fun <S : Server> serve(server: S, init: S.() -> Unit = {}) = server.apply(init).internalStart()


abstract class Server(private val serverName: String) : CoroutineScope {

	val configurationsModule = Module()

	inline fun <reified C> config(path: Path, fileConfiguration: C) {
		configurationsModule.factory<C> { loadConfig<C>(path, fileConfiguration) }
	}

	private val job: Job = Job()

	abstract val httpClient: HttpClient

	val encryptionPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
		initialize(1024)
	}.genKeyPair()
	val verifyToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toByteArray()

	private val handles: MutableSet<PacketHandle> = mutableSetOf()
	val players: MutableSet<Player> = mutableSetOf()

	private fun Connection.handle() = PacketHandle(
		state = State.HANDSHAKING, connection = this, server = this@Server
	).also {
		handles.add(it)

		launch {
			this@handle.socket.awaitClosed()
			handles.removeIf { handle -> handle.connection.socket == this@handle.socket }
		}
	}

	val logger: KLogger
		get() = KotlinLogging.logger(serverName)

	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		logger.error(throwable) { throwable.stackTraceToString() }
	}

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job + coroutineExceptionHandler

	abstract suspend fun startup()

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
			InetSocketAddress(serverConfig.server.host, serverConfig.server.port.toInt())
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
					handle.handleIncoming(this@Server)
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

	abstract suspend fun shutdown()

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
