package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class LongTag(override val name: String?, override var value: Long?) : Tag<Long>() {
	override val type: TagType
		get() = TagType.LONG

	override fun write(buffer: ByteBuffer) {
		buffer.putLong(value ?: error("Value of LongTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.long
	}

	override fun clone(name: String?): Tag<Long> {
		return LongTag(name, value)
	}
}
