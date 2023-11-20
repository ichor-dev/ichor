package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class ShortTag(override val name: String?, override var value: Short?) : Tag<Short>() {
	override val type: TagType
		get() = TagType.SHORT

	override fun write(encoder: Encoder) {
		encoder.encodeShort(value ?: error("Value of ShortTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeShort()
	}

	override fun clone(name: String?): Tag<Short> {
		return ShortTag(name, value)
	}
}