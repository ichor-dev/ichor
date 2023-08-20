package fyi.pauli.ichor.gaia.server

import fyi.pauli.ichor.gaia.config.BaseConfig
import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import java.security.KeyPair
import java.security.KeyPairGenerator
import kotlin.coroutines.CoroutineContext

abstract class Server(private val serverName: String) : CoroutineScope {

	private val job: Job = Job()

	val config: BaseConfig = BaseConfig.loadConfig()

	abstract val httpClient: HttpClient

	val encryptionPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
		initialize(1024)
	}.genKeyPair()
	val verifyToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toByteArray()

	private val handles: MutableSet<PacketHandle> = mutableSetOf()
	val players: MutableSet<Player> = mutableSetOf()

	fun Connection.handle() = PacketHandle(
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
		logger.error { throwable.stackTraceToString() }
	}

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job + coroutineExceptionHandler

	abstract suspend fun startup()

	abstract suspend fun shutdown()

	init {
		Runtime.getRuntime().addShutdownHook(Thread {
			runBlocking {
				shutdown()
				job.cancel()
			}
		})

		Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
			logger.error { throwable }
		}
	}
}
