package dev.pooq.ichor.gaia.networking.packet.receive

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.server.Server

interface PacketReceiver<P : ClientPacket> {

  suspend fun onReceive(packet: P, packetHandle: PacketHandle, server: Server)
}