package fyi.pauli.ichor.gaia.networking.packet.server.login

import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.ServerPacket
import fyi.pauli.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class EncryptionRequest(
	val serverId: String = "",
	val publicKeyLength: Int,
	val publicKey: ByteArray,
	val verifyTokenLength: Int,
	val verifyToken: ByteArray
) : ServerPacket() {

	override fun serialize(): ByteBuffer {
		return uncompressedBuffer {
			string(serverId)
			varInt(publicKeyLength)
			put(publicKey)
			varInt(verifyTokenLength)
			put(verifyToken)
		}
	}

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.LOGIN
}