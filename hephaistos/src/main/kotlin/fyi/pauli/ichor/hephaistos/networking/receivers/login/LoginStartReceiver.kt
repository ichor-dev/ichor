package fyi.pauli.ichor.hephaistos.networking.receivers.login

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.EncryptionRequest
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.networking.receivers.login.EncryptionResponseReceiver.loginStartPackets

object LoginStartReceiver : PacketReceiver<LoginStart> {
	override suspend fun onReceive(packet: LoginStart, packetHandle: PacketHandle, server: Server) {
		loginStartPackets[packetHandle] = packet
		packetHandle.sendPacket(
			EncryptionRequest(
				"", server.encryptionPair.public.encoded, server.verifyToken
			)
		)
	}
}