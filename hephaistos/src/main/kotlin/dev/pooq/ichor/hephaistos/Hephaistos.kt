package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.*
import dev.pooq.ichor.gaia.extensions.error
import dev.pooq.ichor.gaia.extensions.log
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.server.Server
import dev.pooq.ichor.gaia.extensions.debug.debug
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.NullPointerException

object Hephaistos : Server() {

  override suspend fun startup(args: Array<String>) {
    args.forEach { terminal.log(it) }

    val manager = SelectorManager(Dispatchers.Default)
    val serverSocket = aSocket(manager).tcp().bind("127.0.0.1", 25565)

    while (true) {
      val socket = serverSocket.accept()

      val client = socket.handle()

      val read = socket.openReadChannel()

      read.read { buffer ->
        MainScope().launch {
          val packet = ClientPackets.deserializeAndHandle(buffer, client)
          terminal.info(brightGreen("Packet: ${packet.id}"))
          terminal.info(brightYellow("State: ${packet.state}"))
        }

        terminal.log("---".repeat(10))
      }
    }
  }

  override suspend fun shutdown() {
    terminal.log(red("Shutdown"))
  }
}

suspend fun main(args: Array<String>) {
  //Hephaistos.startup(args)
  debug = true
  terminal.log("Log")
  terminal.debug("Debug")
  terminal.error("Error", NullPointerException("An error occured"))
}
