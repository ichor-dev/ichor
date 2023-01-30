package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.client.Receiver

interface PacketHandler<P: ClientPacket> {

  suspend fun onReceive(packet: P, receiver: Receiver)
}