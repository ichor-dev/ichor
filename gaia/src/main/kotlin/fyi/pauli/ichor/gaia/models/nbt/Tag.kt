package fyi.pauli.ichor.gaia.models.nbt

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.short
import java.nio.ByteBuffer

/**
 * Minecraft's Named Binary Tag file format.
 * @see wiki.vg https://wiki.vg/NBT
 */
abstract class Tag<T : Any> {
	abstract val name: String?
	abstract val type: TagType
	abstract var value: T?

	abstract fun write(buffer: ByteBuffer)
	abstract fun read(buffer: ByteBuffer)
	abstract fun clone(name: String?): Tag<T>
}

fun ByteBuffer.putTagString(value: String) {
	short(value.length.toShort())
	rawBytes(value.toByteArray(Charsets.UTF_8))
}

fun ByteBuffer.tagString(): String {
	val length = short().toInt()
	val byteArray = rawBytes(length)

	return String(byteArray, Charsets.UTF_8)
}