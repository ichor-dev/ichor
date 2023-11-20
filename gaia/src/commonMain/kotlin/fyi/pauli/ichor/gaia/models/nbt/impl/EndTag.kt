package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class EndTag(override val name: String? = null) : Tag<Nothing>() {
	override val type: TagType
		get() = TagType.END
	override var value: Nothing? = null

	override fun write(encoder: Encoder) {}
	override fun read(decoder: Decoder) {}
	override fun clone(name: String?): Tag<Nothing> {
		return EndTag(name)
	}
}