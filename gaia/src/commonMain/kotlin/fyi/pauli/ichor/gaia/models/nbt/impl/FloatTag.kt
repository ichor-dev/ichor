package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class FloatTag(override val name: String?, override var value: Float?) : Tag<Float>() {
	override val type: TagType
		get() = TagType.FLOAT

	override fun write(encoder: Encoder) {
		encoder.encodeFloat(value ?: error("Value of FloatTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeFloat()
	}

	override fun clone(name: String?): Tag<Float> {
		return FloatTag(name, value)
	}
}