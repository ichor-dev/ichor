package fyi.pauli.ichor.gaia.networking.packet.receive

import fyi.pauli.ichor.gaia.networking.ClientPacket
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.server.Server

interface PacketReceiver<P : ClientPacket> {

	suspend fun onReceive(packet: P, packetHandle: PacketHandle, server: Server)
}