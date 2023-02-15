package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextColors.red
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.log
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Hephaistos : Server() {

  override suspend fun startup(args: Array<String>) {
    terminal.log(brightGreen("Starting server..."))

    args.forEach { terminal.debug(it) }

    val manager = SelectorManager(Dispatchers.Default)
    val serverSocket = aSocket(manager).tcp().bind("127.0.0.1", 25565)

    terminal.log(brightGreen("Server started successfully!"))

    while (true) {
      val socket = serverSocket.accept()

      val client = socket.handle()

      val read = socket.openReadChannel()

      read.read { buffer ->
        launch {
          ClientPackets.deserializeAndHandle(buffer, client, this@Hephaistos)
        }
      }
    }
  }

  override suspend fun shutdown() {
    terminal.log(red("Shutdown"))
  }

}

suspend fun main(args: Array<String>) {
  Hephaistos.startup(args)
}
