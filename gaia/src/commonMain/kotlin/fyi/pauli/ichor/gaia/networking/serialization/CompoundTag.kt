package fyi.pauli.ichor.gaia.networking.serialization

import fyi.pauli.ichor.gaia.models.nbt.impl.CompoundTag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @author btwonion
 * @since 20/11/2023
 */
public object CompoundTagSerializer : KSerializer<CompoundTag> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("compoundtag", PrimitiveKind.BYTE)

	override fun deserialize(decoder: Decoder): CompoundTag {
		return CompoundTag(null, null).also { it.read(decoder) }
	}

	override fun serialize(encoder: Encoder, value: CompoundTag) {
		value.write(encoder)
	}
}