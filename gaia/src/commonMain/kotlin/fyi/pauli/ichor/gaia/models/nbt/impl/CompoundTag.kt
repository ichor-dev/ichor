package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal typealias CompoundMap = MutableMap<String, Tag<out Any>>

public data class CompoundTag(override val name: String?, override var value: CompoundMap?) : Tag<CompoundMap>() {
	override val type: TagType
		get() = TagType.COMPOUND

	override fun write(encoder: Encoder) {
		value ?: error("Value of CompoundTag is null")

		value!!.forEach { (name, tag) ->
			encoder.encodeByte(tag.type.id.toByte())
			encoder.putTagString(name)

			tag.write(encoder)
		}

		encoder.encodeByte(TagType.END.id.toByte())
	}

	override fun read(decoder: Decoder) {
		val newValue = mutableMapOf<String, Tag<out Any>>()

		var nextType: TagType
		do {
			val id = decoder.decodeByte().toInt()
			nextType = TagType.entries.first { it.id == id }

			if (nextType == TagType.END) break

			val nextName = decoder.tagString()
			val nextTag = nextType.read(decoder, nextName)

			newValue[nextName] = nextTag
		} while (true)

		value = newValue
	}

	override fun clone(name: String?): Tag<CompoundMap> {
		return CompoundTag(name, value)
	}
}