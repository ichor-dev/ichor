package fyi.pauli.ichor.gaia.networking.packet.receive

import fyi.pauli.ichor.gaia.networking.IncomingPacket
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.server.Server

interface PacketReceiver<P : IncomingPacket> {

	suspend fun <P> onReceive(packet: P, packetHandle: PacketHandle, server: Server)
}