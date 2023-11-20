package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class IntTag(override val name: String?, override var value: Int?) : Tag<Int>() {
	override val type: TagType
		get() = TagType.INT

	override fun write(encoder: Encoder) {
		encoder.encodeInt(value ?: error("Value of IntTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeInt()
	}

	override fun clone(name: String?): Tag<Int> {
		return IntTag(name, value)
	}
}