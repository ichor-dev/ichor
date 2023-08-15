package fyi.pauli.ichor.gaia.extensions.bytes

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.networking.INT
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer


internal const val SEGMENT_BITS = 0x7F
internal const val CONTINUE_BIT = 0x80

inline fun OutgoingPacket.buffer(size: Int? = null, applier: ByteBuffer.() -> Unit = {}): ByteBuffer {
	val data = ByteBuffer.allocate(size ?: 1024).apply { varInt(id) }.apply(applier)
	val dataSize = data.position()

	return ByteBuffer.allocate(INT + dataSize).apply {
		varInt(dataSize)
		put(data.array(), 0, dataSize)
	}
}