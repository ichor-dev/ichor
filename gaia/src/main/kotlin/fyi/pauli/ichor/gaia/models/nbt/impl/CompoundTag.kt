package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import java.nio.ByteBuffer

typealias CompoundMap = MutableMap<String, Tag<out Any>>

data class CompoundTag(override val name: String?, override var value: CompoundMap?) : Tag<CompoundMap>() {
	override val type: TagType
		get() = TagType.COMPOUND

	override fun write(buffer: ByteBuffer) {
		value ?: error("Value of CompoundTag is null")

		value!!.forEach { (name, tag) ->
			buffer.put(tag.type.id.toByte())
			buffer.putTagString(name)

			tag.write(buffer)
		}

		buffer.put(TagType.END.id.toByte())
	}

	override fun read(buffer: ByteBuffer) {
		val newValue = mutableMapOf<String, Tag<out Any>>()

		var nextType: TagType
		do {
			nextType = TagType.entries.first { it.id == buffer.get().toInt() }

			if (nextType == TagType.END) break

			val nextName = buffer.tagString
			val nextTag = nextType.read(buffer, nextName)

			newValue[nextName] = nextTag
		} while (true)

		value = newValue
	}

	override fun clone(name: String?): Tag<CompoundMap> {
		return CompoundTag(name, value)
	}
}