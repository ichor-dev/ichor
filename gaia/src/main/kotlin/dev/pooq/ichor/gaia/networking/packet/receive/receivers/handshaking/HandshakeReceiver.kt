package dev.pooq.ichor.gaia.networking.packet.receive.receivers.handshaking

import com.github.ajalt.mordant.rendering.TextColors
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.server.Server

class HandshakeReceiver : PacketReceiver<Handshake> {

  override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle, server: Server) {
    terminal.debug(TextColors.brightBlue("Handshake Packet with next state: ${packet.nextState}, protocol version: ${packet.protocolVersion}"))
    packetHandle.state = packet.nextState
  }
}
