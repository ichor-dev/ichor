package fyi.pauli.ichor.hephaistos.networking.receivers.handshaking

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.server.Server

object HandshakeReceiver : PacketReceiver<Handshake> {
	override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle, server: Server) {
		server.logger.debug {
			"""
				--- Handshake Receiver ---
				Socket: ${packetHandle.connection.socket.remoteAddress}
				Old state: ${packetHandle.state}
				New state: ${packet.nextState}
				-----------------------
			""".trimIndent()
		}

		packetHandle.state = packet.nextState
	}
}