package fyi.pauli.ichor.gaia.models.nbt.impl

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import java.nio.ByteBuffer

data class EndTag(override val name: String? = null) : Tag<Nothing>() {
	override val type: TagType
		get() = TagType.END
	override var value: Nothing? = null

	override fun write(buffer: ByteBuffer) {}
	override fun read(buffer: ByteBuffer) {}
	override fun clone(name: String?): Tag<Nothing> {
		return EndTag(name)
	}
}