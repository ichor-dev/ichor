package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class IntTag(override val name: String?, override var value: Int?) : Tag<Int>() {
	override val type: TagType
		get() = TagType.INT

	override fun write(buffer: ByteBuffer) {
		buffer.putInt(value ?: error("Value of IntTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.int
	}

	override fun clone(name: String?): Tag<Int> {
		return IntTag(name, value)
	}
}