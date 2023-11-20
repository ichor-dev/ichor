package fyi.pauli.ichor.gaia.models.nbt

import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Minecraft's Named Binary Tag file format.
 * @see wiki.vg https://wiki.vg/NBT
 * @author btwonion
 * @since 02/11/2023
 */
public abstract class Tag<T : Any> {
	public abstract val name: String?
	public abstract val type: TagType
	public abstract var value: T?

	public abstract fun write(encoder: Encoder)
	public abstract fun read(decoder: Decoder)
	public abstract fun clone(name: String?): Tag<T>
}

/**
 * Put string in to buffer and prefix it by its length.
 * @param value value to put in
 * @author btwonion
 * @since 02/11/2023
 */
internal fun Encoder.putTagString(value: String) {
	encodeShort(value.length.toShort())
	value.toByteArray(Charsets.UTF_8).forEach { encodeByte(it) }
}

/**
 * Reads string from a buffer.
 * @return [String]
 * @author btwonion
 * @since 02/11/2023
 */
internal fun Decoder.tagString(): String {
	val length = decodeShort().toInt()
	val byteArray = ByteArray(length) { decodeByte() }

	return String(byteArray)
}