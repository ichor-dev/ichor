package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.byte
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.int
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

typealias ListTagList = MutableList<Tag<out Any>>

data class ListTag(override val name: String?, override var value: ListTagList?) : Tag<ListTagList>() {
	override val type: TagType
		get() = TagType.LIST

	override fun write(buffer: ByteBuffer) {
		value ?: error("Value of ListTag is null")

		buffer.byte(value!!.first().type.id.toByte())
		buffer.int(value!!.size)
		value!!.forEach { it.write(buffer) }
	}

	override fun read(buffer: ByteBuffer) {
		val type = TagType.entries.first { it.id == buffer.byte().toInt() }
		val size = buffer.int()

		List(size) { type.read(buffer) }
	}

	override fun clone(name: String?): Tag<ListTagList> {
		return ListTag(name, value)
	}
}