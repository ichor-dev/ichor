package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public typealias TagList = MutableList<Tag<out Any>>

public data class ListTag(override val name: String?, override var value: TagList?) : Tag<TagList>() {
	override val type: TagType
		get() = TagType.LIST

	override fun write(encoder: Encoder) {
		value ?: error("Value of ListTag is null")

		encoder.encodeByte(value!!.first().type.id.toByte())
		encoder.encodeInt(value!!.size)
		value!!.forEach { it.write(encoder) }
	}

	override fun read(decoder: Decoder) {
		val type = TagType.entries.first { it.id == decoder.decodeByte().toInt() }
		val size = decoder.decodeInt()

		List(size) { type.read(decoder) }
	}

	override fun clone(name: String?): Tag<TagList> {
		return ListTag(name, value)
	}
}