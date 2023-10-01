package fyi.pauli.ichor.gaia.extensions.bytes

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.networking.VAR_INT
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.server.finalConfig
import java.nio.ByteBuffer


internal const val SEGMENT_BITS = 0x7F
internal const val CONTINUE_BIT = 0x80

inline fun OutgoingPacket.buffer(size: Int? = null, applier: ByteBuffer.() -> Unit = {}): RawPacket {
	val data =
		ByteBuffer.allocate(size ?: finalConfig?.server?.maxPacketSize ?: 2_097_151).apply { varInt(id) }.apply(applier)
	val dataArray = ByteArray(data.position())
	data.get(dataArray, 0, data.position())

	return RawPacket(dataArray.size, dataArray)
}

fun RawPacket.build(compression: Boolean): ByteBuffer {
	if (!compression) {
		val buffer = ByteBuffer.allocate(VAR_INT + dataSize)
		buffer.varInt(dataSize)
		buffer.rawBytes(data)
		return buffer.flip()
	}

	val compressed = data.compress()
	val unprefixedBuffer = ByteBuffer.allocate(VAR_INT + compressed.size)
	unprefixedBuffer.varInt(compressed.size)
	unprefixedBuffer.rawBytes(compressed)

	val finalBuffer = ByteBuffer.allocate(VAR_INT + unprefixedBuffer.position())
	finalBuffer.varInt(unprefixedBuffer.position())
	finalBuffer.put(unprefixedBuffer)

	return finalBuffer
}

data class RawPacket(val dataSize: Int, val data: ByteArray)