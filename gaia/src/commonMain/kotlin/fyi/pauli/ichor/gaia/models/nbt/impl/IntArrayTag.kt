package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public data class IntArrayTag(override val name: String?, override var value: IntArray?) : Tag<IntArray>() {
	override val type: TagType
		get() = TagType.INT_ARRAY

	override fun write(encoder: Encoder) {
		value ?: error("Value of IntArrayTag is null")
		encoder.encodeInt(value!!.size)
		value!!.forEach { encoder.encodeInt(it) }
	}

	override fun read(decoder: Decoder) {
		value = IntArray(decoder.decodeInt()) { decoder.decodeInt() }
	}

	override fun clone(name: String?): Tag<IntArray> {
		return IntArrayTag(name, value)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as IntArrayTag

		if (name != other.name) return false
		if (value != null) {
			if (other.value == null) return false
			if (!value.contentEquals(other.value)) return false
		} else if (other.value != null) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name?.hashCode() ?: 0
		result = 31 * result + (value?.contentHashCode() ?: 0)
		return result
	}
}