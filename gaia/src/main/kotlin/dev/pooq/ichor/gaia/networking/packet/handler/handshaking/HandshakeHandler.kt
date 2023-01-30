package dev.pooq.ichor.gaia.networking.packet.handler.handshaking

import com.github.ajalt.mordant.rendering.TextColors.*
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.client.Receiver
import dev.pooq.ichor.gaia.networking.packet.PacketHandler
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake

class HandshakeHandler : PacketHandler<Handshake> {

  override suspend fun onReceive(packet: Handshake, receiver: Receiver) {

  }

}