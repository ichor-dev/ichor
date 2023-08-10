package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.byteArray
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Reponse packet for EncryptionRequest
 *
 * @param sharedSecret Shared Secret value, encrypted with the server's public key.
 * @param verifyToken Verify Token value, encrypted with the same public key as the shared secret.
 */
data class EncryptionResponse(
	var sharedSecret: ByteArray, var verifyToken: ByteArray
) : IncomingPacket() {
	companion object : PacketProcessor<EncryptionResponse>() {
		override suspend fun deserialize(buffer: ByteBuffer): EncryptionResponse {
			val sharedSecretLength = buffer.varInt()
			val sharedSecret = buffer.byteArray(sharedSecretLength)
			val verifyTokenLength = buffer.varInt()
			val verifyToken = buffer.byteArray(verifyTokenLength)

			return EncryptionResponse(
				sharedSecret, verifyToken
			)
		}
	}
}