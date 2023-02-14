package dev.pooq.ichor.gaia.server

import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

abstract class Server : CoroutineScope {

  private val job: Job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Default + job

  var terminal: Terminal

  init {
    Runtime.getRuntime().addShutdownHook(Thread {
      runBlocking {
        shutdown()
        job.cancel()
      }
    })

    terminal = terminal()
  }

  private val handles: HashSet<PacketHandle> = hashSetOf()

  fun Socket.handle() = handles.find { it.socket == this } ?: PacketHandle(
    state = State.HANDSHAKING,
    socket = this@handle,
    coroutineContext = this@Server.coroutineContext
  ).apply {
    handles.add(this)
  }

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()
}
