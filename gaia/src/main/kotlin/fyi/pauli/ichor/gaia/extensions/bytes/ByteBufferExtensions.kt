package fyi.pauli.ichor.gaia.extensions.bytes

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.networking.VAR_INT
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer


internal const val SEGMENT_BITS = 0x7F
internal const val CONTINUE_BIT = 0x80

inline fun OutgoingPacket.buffer(size: Int? = null, applier: ByteBuffer.() -> Unit = {}): RawPacket {
	val data = ByteBuffer.allocate(size ?: 2097151).apply { varInt(id) }.apply(applier)
	val dataSize = data.position()

	return RawPacket(dataSize, data.limit(dataSize))
}

fun RawPacket.build(compression: Boolean): ByteBuffer {
	if (!compression) {
		val buffer = ByteBuffer.allocate(VAR_INT + dataSize)
		buffer.varInt(dataSize)
		buffer.put(data.array(), 0, dataSize)
		return buffer.flip()
	}

	// TODO: fix compression
	val dataBuffer = ByteBuffer.allocate(dataSize)
	dataBuffer.put(data)
	val uncompressedBufferLength = dataBuffer.position()
	val dataArray = dataBuffer.array()
	val compressedBufferLength = dataArray.compress()
	val finalBuffer = ByteBuffer.allocate(VAR_INT + VAR_INT + compressedBufferLength)
	finalBuffer.varInt(VAR_INT + compressedBufferLength)
	finalBuffer.varInt(uncompressedBufferLength)
	finalBuffer.put(dataArray, 0, dataSize)
	return finalBuffer
}

data class RawPacket(val dataSize: Int, val data: ByteBuffer)