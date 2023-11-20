package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class LongTag(override val name: String?, override var value: Long?) : Tag<Long>() {
	override val type: TagType
		get() = TagType.LONG

	override fun write(encoder: Encoder) {
		encoder.encodeLong(value ?: error("Value of LongTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeLong()
	}

	override fun clone(name: String?): Tag<Long> {
		return LongTag(name, value)
	}
}
