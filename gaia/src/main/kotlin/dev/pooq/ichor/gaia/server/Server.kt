package dev.pooq.ichor.gaia.server

import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class Server {

  init {
    Runtime.getRuntime().addShutdownHook(Thread {
      MainScope().launch {
        shutdown()
      }
    })
  }

  private val handles: HashSet<PacketHandle> = hashSetOf()

  fun Socket.handle() = handles.find { it.socket == this } ?: run {
    val handle = PacketHandle(state = State.STATUS, socket = this)
    handles.add(handle)
    handle
  }

  val terminal = terminal()

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()
}
