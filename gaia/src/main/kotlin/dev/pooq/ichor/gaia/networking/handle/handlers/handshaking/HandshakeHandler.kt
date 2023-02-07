package dev.pooq.ichor.gaia.networking.handle.handlers.handshaking

import com.github.ajalt.mordant.rendering.TextColors.*
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.PacketHandler
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake

class HandshakeHandler : PacketHandler<Handshake> {

  override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle) {
    terminal.info("""${green}("Handshake:")
      ${brightYellow("Protocol Version: ${packet.protocolVersion}")}
      ${brightYellow("Server Address: ${packet.serverAddress}")}
      ${brightYellow("Server Port: ${packet.serverPort}")}
      ${brightYellow("Next State: ${packet.nextState}")}
      ${brightYellow("Client State: ${packetHandle.state}")}
    """.trimIndent())
  }
}
