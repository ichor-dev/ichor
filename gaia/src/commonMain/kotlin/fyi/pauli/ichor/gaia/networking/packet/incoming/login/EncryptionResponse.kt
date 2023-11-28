package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

/**
 * Response packet for EncryptionRequest
 *
 * @param sharedSecret Shared Secret value, encrypted with the server's public key.
 * @param verifyToken Verify Token value, encrypted with the same public key as the shared secret.
 */
@Serializable
public data class EncryptionResponse(
	var sharedSecret: ByteArray, var verifyToken: ByteArray
) : IncomingPacket {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as EncryptionResponse

		if (!sharedSecret.contentEquals(other.sharedSecret)) return false
		if (!verifyToken.contentEquals(other.verifyToken)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = sharedSecret.contentHashCode()
		result = 31 * result + verifyToken.contentHashCode()
		return result
	}
}