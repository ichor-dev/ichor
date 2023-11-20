package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class StringTag(override val name: String?, override var value: String?) : Tag<String>() {
	override val type: TagType
		get() = TagType.STRING

	override fun write(encoder: Encoder) {
		encoder.putTagString(value ?: error("Value of StringTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.tagString()
	}

	override fun clone(name: String?): Tag<String> {
		return StringTag(name, value)
	}
}