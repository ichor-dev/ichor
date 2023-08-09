package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class DoubleTag(override val name: String?, override var value: Double?) : Tag<Double>() {
	override val type: TagType
		get() = TagType.DOUBLE

	override fun write(buffer: ByteBuffer) {
		buffer.putDouble(value ?: error("Value of DoubleTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.double
	}

	override fun clone(name: String?): Tag<Double> {
		return DoubleTag(name, value)
	}
}