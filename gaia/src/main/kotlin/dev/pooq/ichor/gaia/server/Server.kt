package dev.pooq.ichor.gaia.server

import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
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

  fun Socket.handle() = handles.find { it.socket == this } ?: run {
    val handle = PacketHandle(state = State.STATUS, socket = this)
    handles.add(handle)
    handle
  }

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()
}
