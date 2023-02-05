package dev.pooq.ichor.gaia.server

import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
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

  val packetHandles: HashSet<PacketHandle> = hashSetOf()

  val terminal = terminal()

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()

}