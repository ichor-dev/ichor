package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.byte
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class ByteTag(override val name: String?, override var value: Byte?) : Tag<Byte>() {
	override val type: TagType
		get() = TagType.BYTE

	override fun write(buffer: ByteBuffer) {
		buffer.byte(value ?: error("Value of ByteTag is null"))
	}

	override fun read(buffer: ByteBuffer) {
		value = buffer.byte()
	}

	override fun clone(name: String?): Tag<Byte> {
		return ByteTag(name, value)
	}
}