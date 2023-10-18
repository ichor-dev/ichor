package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.ichor.gaia.server.Server

interface PacketReceiver<P : IncomingPacket> {

	suspend fun onReceive(packet: P, packetHandle: PacketHandle, server: Server)
}