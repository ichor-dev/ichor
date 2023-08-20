package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.int
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.long
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class LongArrayTag(override val name: String?, override var value: LongArray?) : Tag<LongArray>() {
	override val type: TagType
		get() = TagType.LONG_ARRAY

	override fun write(buffer: ByteBuffer) {
		value ?: error("Value of LongArrayTag is null")
		buffer.putInt(value!!.size)
		value!!.forEach { buffer.long(it) }
	}

	override fun read(buffer: ByteBuffer) {
		value = LongArray(buffer.int()) { buffer.long() }
	}

	override fun clone(name: String?): Tag<LongArray> {
		return LongArrayTag(name, value)
	}
}