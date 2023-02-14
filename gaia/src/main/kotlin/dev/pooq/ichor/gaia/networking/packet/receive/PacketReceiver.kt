package dev.pooq.ichor.gaia.networking.packet.receive

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.PacketHandle

interface PacketReceiver<P : ClientPacket> {

  suspend fun onReceive(packet: P, packetHandle: PacketHandle)
}