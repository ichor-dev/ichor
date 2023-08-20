package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.byteArray
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Response packet for EncryptionRequest
 *
 * @param sharedSecret Shared Secret value, encrypted with the server's public key.
 * @param verifyToken Verify Token value, encrypted with the same public key as the shared secret.
 */
data class EncryptionResponse(
	var sharedSecret: ByteArray, var verifyToken: ByteArray
) : IncomingPacket() {
	companion object : PacketProcessor<EncryptionResponse>() {
		override suspend fun deserialize(buffer: ByteBuffer): EncryptionResponse {
			val sharedSecret = buffer.byteArray()
			val verifyToken = buffer.byteArray()

			return EncryptionResponse(
				sharedSecret, verifyToken
			)
		}
	}
}