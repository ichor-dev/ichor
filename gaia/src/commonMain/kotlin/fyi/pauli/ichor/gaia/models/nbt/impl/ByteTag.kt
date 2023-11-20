package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class ByteTag(override val name: String?, override var value: Byte?) : Tag<Byte>() {
	override val type: TagType
		get() = TagType.BYTE

	override fun write(encoder: Encoder) {
		encoder.encodeByte(value ?: error("Value of ByteTag is null"))
	}

	override fun read(decoder: Decoder) {
		value = decoder.decodeByte()
	}

	override fun clone(name: String?): Tag<Byte> {
		return ByteTag(name, value)
	}
}