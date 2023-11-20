package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class DoubleTag(override val name: String?, override var value: Double?) : Tag<Double>() {
	override val type: TagType
		get() = TagType.DOUBLE

	override fun write(encoder: Encoder) {
		encoder.encodeDouble(value ?: error("Value of DoubleTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeDouble()
	}

	override fun clone(name: String?): Tag<Double> {
		return DoubleTag(name, value)
	}
}