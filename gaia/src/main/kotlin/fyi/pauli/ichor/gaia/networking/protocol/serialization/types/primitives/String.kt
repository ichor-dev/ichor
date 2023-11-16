package fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives

import fyi.pauli.ichor.gaia.networking.protocol.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.ichor.gaia.networking.protocol.exceptions.MinecraftProtocolEncodingException
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarIntSerializer.readVarInt
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarIntSerializer.writeVarInt

/**
 * @author btwonion
 * @since 11/11/2023
 * 
 * Internal class for de-/serializing Minecraft formatted strings.
 */
internal object MinecraftStringEncoder {
	/**
	 * The default max string length Minecraft uses.
	 */
	const val MAX_STRING_LENGTH = 32767

	@ExperimentalStdlibApi
	internal inline fun readString(
		maxLength: Int = MAX_STRING_LENGTH,
		readByte: () -> Byte,
		readBytes: (length: Int) -> ByteArray,
	): String {
		val length: Int = readVarInt(readByte)
		return if (length > maxLength * 4) {
			throw MinecraftProtocolDecodingException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
		} else if (length < 0) {
			throw MinecraftProtocolDecodingException("The received encoded string buffer length is less than zero! Weird string!")
		} else {
			val stringBuffer = readBytes(length).decodeToString()
			if (stringBuffer.length > maxLength) {
				throw MinecraftProtocolDecodingException("The received string length is longer than maximum allowed ($length > $maxLength)")
			} else {
				stringBuffer
			}
		}
	}

	internal inline fun writeString(
		string: String, writeByte: (Byte) -> Unit, writeFully: (ByteArray) -> Unit
	) {
		val bytes = string.toByteArray()

		if (bytes.size > MAX_STRING_LENGTH) {
			throw MinecraftProtocolEncodingException("String too big (was " + bytes.size + " bytes encoded, max " + 32767 + ")")
		} else {
			writeVarInt(bytes.size, writeByte)
			writeFully(bytes)
		}
	}
}