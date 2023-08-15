package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.int
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class IntArrayTag(override val name: String?, override var value: IntArray?) : Tag<IntArray>() {
	override val type: TagType
		get() = TagType.INT_ARRAY

	override fun write(buffer: ByteBuffer) {
		value ?: error("Value of IntArrayTag is null")
		buffer.putInt(value!!.size)
		value!!.forEach { buffer.int(it) }
	}

	override fun read(buffer: ByteBuffer) {
		value = IntArray(buffer.int()) { buffer.int() }
	}

	override fun clone(name: String?): Tag<IntArray> {
		return IntArrayTag(name, value)
	}
}