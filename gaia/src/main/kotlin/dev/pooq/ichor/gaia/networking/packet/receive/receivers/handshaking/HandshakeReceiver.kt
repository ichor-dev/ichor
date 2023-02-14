package dev.pooq.ichor.gaia.networking.packet.receive.receivers.handshaking

import com.github.ajalt.mordant.rendering.TextColors.brightYellow
import com.github.ajalt.mordant.rendering.TextColors.green
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake

class HandshakeReceiver : PacketReceiver<Handshake> {

  override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle) {
    terminal.info(
      """${green}("Handshake:")
      ${brightYellow("Protocol Version: ${packet.protocolVersion}")}
      ${brightYellow("Server Address: ${packet.serverAddress}")}
      ${brightYellow("Server Port: ${packet.serverPort}")}
      ${brightYellow("Next State: ${packet.nextState}")}
      ${brightYellow("Client State: ${packetHandle.state}")}
    """.trimIndent()
    )

    packetHandle.state = packet.nextState
  }
}
