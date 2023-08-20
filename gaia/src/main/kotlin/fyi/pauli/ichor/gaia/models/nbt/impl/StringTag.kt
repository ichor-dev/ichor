package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import java.nio.ByteBuffer

data class StringTag(override val name: String?, override var value: String?) : Tag<String>() {
	override val type: TagType
		get() = TagType.STRING

	override fun write(buffer: ByteBuffer) {
		buffer.putTagString(value ?: error("Value of StringTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.tagString()
	}

	override fun clone(name: String?): Tag<String> {
		return StringTag(name, value)
	}
}