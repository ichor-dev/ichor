package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.short
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class ShortTag(override val name: String?, override var value: Short?) : Tag<Short>() {
	override val type: TagType
		get() = TagType.SHORT

	override fun write(buffer: ByteBuffer) {
		buffer.short(value ?: error("Value of ShortTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.short()
	}

	override fun clone(name: String?): Tag<Short> {
		return ShortTag(name, value)
	}
}