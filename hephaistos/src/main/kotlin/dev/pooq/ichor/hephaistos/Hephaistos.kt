package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.*
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*

object Hephaistos : Server() {

  override suspend fun startup(args: Array<String>) {
    val manager = SelectorManager(Dispatchers.Default)
    val serverSocket = aSocket(manager).tcp().bind("127.0.0.1", 25565)

    while (true) {
      val socket = serverSocket.accept()

      val client = packetHandles.stream().filter {
        it.socket == socket
      }.findFirst().orElseGet {
        val packetHandle = PacketHandle(State.STATUS, socket)
        packetHandles.add(packetHandle)
        packetHandle
      }

      val read = socket.openReadChannel()

      read.read { buffer ->
        MainScope().launch {
          val packet = ClientPackets.deserializeAndHandle(buffer, client)
          terminal.info(brightGreen("Packet: ${packet.id}"))
          terminal.info(brightYellow("State: ${packet.state}"))
        }

        terminal.info("---".repeat(10))
      }
    }
  }

  override suspend fun shutdown() {
    terminal().info(red("Shutdown"))
  }
}

suspend fun main(args: Array<String>) {
  Hephaistos.startup(args)
}