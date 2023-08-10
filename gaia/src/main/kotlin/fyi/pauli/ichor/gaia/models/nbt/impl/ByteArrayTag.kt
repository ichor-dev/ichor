package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class ByteArrayTag(override val name: String?, override var value: ByteArray?) : Tag<ByteArray>() {
	override val type: TagType
		get() = TagType.BYTE_ARRAY

	override fun write(buffer: ByteBuffer) {
		value ?: error("Value of ByteArrayTag is null")
		buffer.putInt(value!!.size)
		buffer.put(value)
	}

	override fun read(buffer: ByteBuffer) {
		val length = buffer.int

		value = ByteArray(length)
		buffer.get(value, 0, length)
	}

	override fun clone(name: String?): Tag<ByteArray> {
		return ByteArrayTag(name, value)
	}
}