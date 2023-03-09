package dev.pooq.ichor.gaia.server

import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.entity.player.Player
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.error
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.security.KeyPair
import java.security.KeyPairGenerator
import kotlin.coroutines.CoroutineContext

abstract class Server : CoroutineScope {

  private val job: Job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Default + job

  var terminal: Terminal

  val encryptionPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
    initialize(1024)
  }.genKeyPair()
  val verifyToken = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toByteArray()

  init {
    Runtime.getRuntime().addShutdownHook(Thread {
      runBlocking {
        shutdown()
        job.cancel()
      }
    })

    terminal = terminal()

    terminal.debug("Debug is enabled")

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
      terminal.error("${throwable.message} | Thread: ${thread.name}", throwable)
    }
  }

  private val handles: HashSet<PacketHandle> = hashSetOf()
  val players: HashSet<Player> = hashSetOf()

  fun Socket.handle(connection: Connection) = handles.find { it.connection.socket == this } ?: PacketHandle(
    state = State.HANDSHAKING,
    connection = connection,
    coroutineContext = this@Server.coroutineContext
  ).apply {
    handles.add(this)
  }

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()
}
