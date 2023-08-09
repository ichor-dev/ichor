package fyi.pauli.ichor.gaia.models.nbt

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
	apply {
		putShort(value.length.toShort())
		put(value.toByteArray())
	}
}

val ByteBuffer.tagString: String
	get() {
		val length = short.toInt()
		val byteArray = ByteArray(length).also { get(it, 0, length) }

		return String(byteArray)
	}