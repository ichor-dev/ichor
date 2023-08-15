package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.float
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class FloatTag(override val name: String?, override var value: Float?) : Tag<Float>() {
	override val type: TagType
		get() = TagType.FLOAT

	override fun write(buffer: ByteBuffer) {
		buffer.float(value ?: error("Value of FloatTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.float()
	}

	override fun clone(name: String?): Tag<Float> {
		return FloatTag(name, value)
	}
}