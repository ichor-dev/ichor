package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.byteArray
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Packet, used to authenticate the server.
 *
 * @param serverId Appears to be empty.
 * @param publicKey The server's public key, in bytes.
 * @param verifyToken A sequence of random bytes generated by the server.
 */
data class EncryptionRequest(
	var serverId: String,
	var publicKey: ByteArray,
	var verifyToken: ByteArray
) : OutgoingPacket() {

	override fun serialize(): ByteBuffer {
		return buffer {
			string(serverId)
			byteArray(publicKey)
			byteArray(verifyToken)
		}
	}

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.LOGIN
}