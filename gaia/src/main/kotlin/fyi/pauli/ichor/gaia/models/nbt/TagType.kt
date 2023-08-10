package fyi.pauli.ichor.gaia.models.nbt

import fyi.pauli.ichor.gaia.models.nbt.impl.*
import java.nio.ByteBuffer

enum class TagType(val id: Int) {
	END(0),
	BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	BYTE_ARRAY(7),
	STRING(8),
	LIST(9),
	COMPOUND(10),
	INT_ARRAY(11),
	LONG_ARRAY(12);

	fun read(buffer: ByteBuffer, name: String? = null): Tag<*> {
		return when (this) {
			END -> EndTag()
			BYTE -> ByteTag(name, null).also { read(buffer) }
			SHORT -> ShortTag(name, null).also { read(buffer) }
			INT -> IntTag(name, null).also { read(buffer) }
			LONG -> LongTag(name, null).also { read(buffer) }
			FLOAT -> FloatTag(name, null).also { read(buffer) }
			DOUBLE -> DoubleTag(name, null).also { read(buffer) }
			BYTE_ARRAY -> ByteArrayTag(name, null).also { read(buffer) }
			STRING -> StringTag(name, null).also { read(buffer) }
			LIST -> ListTag(name, null).also { read(buffer) }
			COMPOUND -> CompoundTag(name, null).also { read(buffer) }
			INT_ARRAY -> IntArrayTag(name, null).also { read(buffer) }
			LONG_ARRAY -> LongArrayTag(name, null).also { read(buffer) }
		}
	}
}