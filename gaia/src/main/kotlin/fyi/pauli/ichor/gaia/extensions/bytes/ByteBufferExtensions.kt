package fyi.pauli.ichor.gaia.extensions.bytes

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.networking.VAR_INT
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.server.finalConfig
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer


internal const val SEGMENT_BITS = 0x7F
internal const val CONTINUE_BIT = 0x80

inline fun OutgoingPacket.buffer(size: Int? = null, applier: ByteBuffer.() -> Unit = {}): RawPacket {
	val data = ByteBuffer.allocate(size ?: finalConfig?.server?.maxPacketSize ?: 2_097_151).apply(applier)

	return RawPacket(id, ByteBuffer.allocate(data.position()).apply(applier))
}

fun RawPacket.buildCompressed(): ByteBuffer {
	val idBuffer = ByteBuffer.allocate(VAR_INT + data.position())
	idBuffer.varInt(id)
	idBuffer.put(data.array())
	val idArray = idBuffer.rawBytes()?.compress() ?: throw BufferUnderflowException()
	val compressedIdBuffer = ByteBuffer.allocate(VAR_INT + idArray.size)
	compressedIdBuffer.varInt(idBuffer.position())
	compressedIdBuffer.put(idArray)

	val finalBuffer = ByteBuffer.allocate(VAR_INT + compressedIdBuffer.position())
	finalBuffer.varInt(compressedIdBuffer.position())
	finalBuffer.put(compressedIdBuffer.array())

	return finalBuffer
}

fun RawPacket.build(): ByteBuffer {
	val idBuffer = ByteBuffer.allocate(VAR_INT + data.position())
	idBuffer.varInt(id)
	idBuffer.put(data.array())

	val finalBuffer = ByteBuffer.allocate(VAR_INT + idBuffer.position())
	finalBuffer.varInt(idBuffer.position())
	finalBuffer.put(idBuffer.array().take(data.position()).toByteArray())

	return finalBuffer.flip()
}

data class RawPacket(val id: Int, val data: ByteBuffer)