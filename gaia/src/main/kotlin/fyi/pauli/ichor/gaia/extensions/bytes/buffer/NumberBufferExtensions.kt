package fyi.pauli.ichor.gaia.extensions.bytes.buffer

import fyi.pauli.ichor.gaia.extensions.bytes.CONTINUE_BIT
import fyi.pauli.ichor.gaia.extensions.bytes.SEGMENT_BITS
import java.nio.ByteBuffer
import kotlin.experimental.and


fun ByteBuffer.unsignedShort(value: Short) {
	putShort((value and 0xFFFF.toShort()))
}

fun ByteBuffer.unsignedShort(): Short {
	return getShort() and 0xFFFF.toShort()
}

fun ByteBuffer.varInt(value: Int) {
	var processingValue = value

	while (true) {
		if (processingValue and SEGMENT_BITS.inv() == 0) {
			put(processingValue.toByte())
			return
		}
		put(((processingValue and SEGMENT_BITS) or CONTINUE_BIT).toByte())

		processingValue = processingValue ushr 7
	}
}

fun ByteBuffer.varInt(): Int {
	var value = 0
	var size = 0
	var byte: Byte

	do {
		byte = get()
		value = value or ((byte.toInt() and 0x7F) shl (size++ * 7))
	} while (byte.toInt() and 0x80 != 0)

	return value
}


fun ByteBuffer.varIntArray(value: IntArray) {
	varInt(value.size)
	value.forEach { varInt(it) }
}

fun ByteBuffer.varIntArray(): IntArray {
	return IntArray(varInt()) { varInt() }
}

fun ByteBuffer.varLong(value: Long) {
	var processingValue = value

	while (true) {
		if ((processingValue and (SEGMENT_BITS.toLong().inv())) == 0L) {
			put(processingValue.toByte())
			return
		}
		put(((processingValue and SEGMENT_BITS.toLong()) or CONTINUE_BIT.toLong()).toByte())

		processingValue = processingValue ushr 7
	}
}

fun ByteBuffer.varLong(): Long {
	var value: Long = 0
	var position = 0
	var currentByte: Byte

	while (true) {
		currentByte = get()
		value = value or ((currentByte and SEGMENT_BITS.toByte()).toLong() shl position)
		if ((currentByte and CONTINUE_BIT.toByte()).toInt() == 0) break
		position += 7
		if (position >= 64) throw RuntimeException("VarLong is too big")
	}

	return value
}

fun ByteBuffer.varLongArray(value: LongArray) {
	varInt(value.size)
	value.forEach { varLong(it) }
}

fun ByteBuffer.varLongArray(): LongArray {
	return LongArray(varInt()) { varLong() }
}