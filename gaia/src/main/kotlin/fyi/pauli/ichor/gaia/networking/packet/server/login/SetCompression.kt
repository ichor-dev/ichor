package fyi.pauli.ichor.gaia.networking.packet.server.login

import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.ServerPacket
import fyi.pauli.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class SetCompression(val threshold: Int) : ServerPacket() {
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