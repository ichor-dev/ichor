package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.byteArray
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

class EncryptionResponse(
	var sharedSecretLength: Int,
	var sharedSecret: ByteArray,
	var verifyTokenLength: Int,
	var verifyToken: ByteArray
) : IncomingPacket() {

	companion object :
		PacketProcessor<EncryptionResponse>() {
		override suspend fun deserialize(byteBuffer: ByteBuffer): EncryptionResponse {
			val sharedSecretLength = byteBuffer.varInt()
			val sharedSecret = byteBuffer.byteArray(sharedSecretLength)
			val verifyTokenLength = byteBuffer.varInt()
			val verifyToken = byteBuffer.byteArray(verifyTokenLength)

			return EncryptionResponse(
				sharedSecretLength,
				sharedSecret,
				verifyTokenLength,
				verifyToken
			)
		}
	}
}