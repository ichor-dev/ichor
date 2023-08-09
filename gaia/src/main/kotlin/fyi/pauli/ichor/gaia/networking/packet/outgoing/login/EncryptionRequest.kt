package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

data class EncryptionRequest(
	var serverId: String = "",
	var publicKeyLength: Int,
	var publicKey: ByteArray,
	var verifyTokenLength: Int,
	var verifyToken: ByteArray
) : OutgoingPacket() {

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