package fyi.pauli.ichor.gaia.models.nbt

import fyi.pauli.ichor.gaia.models.nbt.impl.*
import kotlinx.serialization.encoding.Decoder

public enum class TagType(internal val id: Int) {
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

	public fun read(decoder: Decoder, name: String? = null): Tag<*> {
		return when (this) {
			END -> EndTag()
			BYTE -> ByteTag(name, null).also { it.read(decoder) }
			SHORT -> ShortTag(name, null).also { it.read(decoder) }
			INT -> IntTag(name, null).also { it.read(decoder) }
			LONG -> LongTag(name, null).also { it.read(decoder) }
			FLOAT -> FloatTag(name, null).also { it.read(decoder) }
			DOUBLE -> DoubleTag(name, null).also { it.read(decoder) }
			BYTE_ARRAY -> ByteArrayTag(name, null).also { it.read(decoder) }
			STRING -> StringTag(name, null).also { it.read(decoder) }
			LIST -> ListTag(name, null).also { it.read(decoder) }
			COMPOUND -> CompoundTag(name, null).also { it.read(decoder) }
			INT_ARRAY -> IntArrayTag(name, null).also { it.read(decoder) }
			LONG_ARRAY -> LongArrayTag(name, null).also { it.read(decoder) }
		}
	}
}