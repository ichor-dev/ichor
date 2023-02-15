package dev.pooq.ichor.gaia.networking.packet.receive.receivers.handshaking

import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.server.Server

class HandshakeReceiver : PacketReceiver<Handshake> {

  override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle, server: Server) {
    packetHandle.state = packet.nextState
    server.terminal.debug("Changed state of ${packetHandle.socket.remoteAddress} to ${packetHandle.state}")
  }
}
