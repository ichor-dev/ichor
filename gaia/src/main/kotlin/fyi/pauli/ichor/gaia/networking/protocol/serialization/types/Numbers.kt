package fyi.pauli.ichor.gaia.networking.protocol.serialization.types

import kotlin.experimental.and
import kotlin.experimental.or

/**
 * @author btwonion
 * @since 11/11/2023
 */

enum class MinecraftNumberType {
	DEFAULT, UNSIGNED, VAR
}

internal object VarIntEncoder {
	internal inline fun readVarInt(
		readByte: () -> Byte
	): Int {
		var numRead = 0
		var result = 0
		var read: Byte
		do {
			read = readByte()
			val value = (read and 127).toInt()
			result = result or (value shl 7 * numRead)
			numRead++
			if (numRead > 5) {
				throw RuntimeException("VarInt is too big")
			}
		} while (read and 128.toByte() != 0.toByte())
		return result
	}

	internal inline fun writeVarInt(
		value: Int,
		writeByte: (Byte) -> Unit,
	) {
		var v = value
		do {
			var temp = (v and 127).toByte()
			// Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
			v = v ushr 7
			if (v != 0) {
				temp = temp or 128.toByte()
			}
			writeByte(temp)
		} while (v != 0)
	}

	fun varIntBytesCount(
		value: Int,
	): Int {
		var counter = 0
		writeVarInt(value) { counter++ }
		return counter
	}
}

internal object VarLongDecoder {
	internal inline fun readVarInt(
		readByte: () -> Byte
	): Int {
		var numRead = 0
		var result = 0
		var read: Byte
		do {
			read = readByte()
			val value = (read and 127).toInt()
			result = result or (value shl 7 * numRead)
			numRead++
			if (numRead > 5) {
				throw RuntimeException("VarInt is too big")
			}
		} while (read and 128.toByte() != 0.toByte())
		return result
	}

	internal inline fun writeVarInt(
		value: Int,
		writeByte: (Byte) -> Unit,
	) {
		var v = value
		do {
			var temp = (v and 127).toByte()
			// Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
			v = v ushr 7
			if (v != 0) {
				temp = temp or 128.toByte()
			}
			writeByte(temp)
		} while (v != 0)
	}

	fun varIntBytesCount(
		value: Int,
	): Int {
		var counter = 0
		writeVarInt(value) { counter++ }
		return counter
	}
}