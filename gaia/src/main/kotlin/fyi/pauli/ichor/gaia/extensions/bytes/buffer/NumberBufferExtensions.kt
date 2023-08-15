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
	when {
		value and (-0x1 shl 7) == 0 -> put(value.toByte())
		value and (-0x1 shl 14) == 0 -> putShort((value and 0x7F or 0x80 shl 8 or (value ushr 7)).toShort())
		value and (-0x1 shl 21) == 0 -> {
			val index = position()
			put(index, (value and 0x7F or 0x80).toByte())
			put(index + 1, (value ushr 7 and 0x7F or 0x80).toByte())
			put(index + 2, (value ushr 14).toByte())
		}

		value and (-0x1 shl 28) == 0 -> putInt(value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21))
		else -> {
			val index = position()
			putInt(
				index,
				value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)
			)
			put(index + 4, (value ushr 28).toByte())
		}
	}
}

fun ByteBuffer.varInt(): Int {
	var result = 0

	var shift = 0
	while (true) {
		val b: Byte = get(position() + 1)
		result = result or ((b.toInt() and 0x7f) shl shift)
		if (b >= 0) return result
		shift += 7
	}
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
		if ((processingValue and (SEGMENT_BITS.toLong()).inv()) == 0L) {
			put(processingValue.toByte())
		}
		put((processingValue and SEGMENT_BITS.toLong() or CONTINUE_BIT.toLong()).toByte())
		processingValue = processingValue ushr 7
	}
}

fun ByteBuffer.varLong(): Long {
	var value: Long = 0
	var position = 0
	var currentByte: Byte

	while (true) {
		currentByte = get()
		value = value or (currentByte.toLong() and SEGMENT_BITS.toLong()) shl position
		if ((currentByte and CONTINUE_BIT.toByte()) == 0.toByte()) break
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