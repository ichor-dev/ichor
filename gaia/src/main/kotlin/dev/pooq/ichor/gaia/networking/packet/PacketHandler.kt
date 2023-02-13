package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.handle.PacketHandle

interface PacketHandler<P : ClientPacket> {

  suspend fun onReceive(packet: P, packetHandle: PacketHandle)
}