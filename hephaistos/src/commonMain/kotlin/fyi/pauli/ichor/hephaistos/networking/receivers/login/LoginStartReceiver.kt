package fyi.pauli.ichor.hephaistos.networking.receivers.login

import dev.whyoleg.cryptography.algorithms.asymmetric.RSA
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.EncryptionRequest
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.networking.receivers.login.EncryptionResponseReceiver.loginStartPackets

public object LoginStartReceiver : PacketReceiver<LoginStart> {
	override suspend fun onReceive(packet: LoginStart, packetHandle: PacketHandle, server: Server) {
		loginStartPackets[packetHandle] = packet
		packetHandle.sendPacket(
			EncryptionRequest(
				"", server.encryptionPair.publicKey.encodeTo(RSA.PublicKey.Format.PEM), server.verifyToken
			)
		)
	}
}