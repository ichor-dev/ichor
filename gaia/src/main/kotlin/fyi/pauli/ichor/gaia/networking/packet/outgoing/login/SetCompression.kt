package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

data class SetCompression(var threshold: Int) : OutgoingPacket() {
	override fun serialize(): ByteBuffer {
		return uncompressedBuffer {
			varInt(threshold)
		}
	}

	override val id: Int
		get() = 0x03
	override val state: State
		get() = State.LOGIN
}