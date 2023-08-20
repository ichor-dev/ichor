package fyi.pauli.ichor.hephaistos.networking.receivers.handshaking

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.server.Server

object HandshakeReceiver : PacketReceiver<Handshake> {
	override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle, server: Server) {
		server.logger.debug {
			"Switched state from ${packetHandle.state} to ${packet.nextState}."
		}

		packetHandle.state = packet.nextState
	}
}
