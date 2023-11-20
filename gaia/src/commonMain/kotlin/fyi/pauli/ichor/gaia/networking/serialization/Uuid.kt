package fyi.pauli.ichor.gaia.networking.serialization

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * @author btwonion
 * @since 14/11/2023
 *
 * Long serializer for the Uuid object.
 * Important cause Minecraft only accepts Uuids encoded like this.
 */
public object UuidLongSerializer : KSerializer<Uuid> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("uuid") {
		element<Long>("msb")
		element<Long>("lsb")
	}

	override fun serialize(encoder: Encoder, value: Uuid) {
		encoder.encodeStructure(descriptor) {
			encodeLongElement(descriptor, 0, value.mostSignificantBits)
			encodeLongElement(descriptor, 1, value.leastSignificantBits)
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Uuid {
		return decoder.decodeStructure(descriptor) {
			var mostSignificationBits: Long? = null
			var leastSignificationBits: Long? = null

			if (decodeSequentially()) {
				mostSignificationBits = decodeLongElement(descriptor, 0)
				leastSignificationBits = decodeLongElement(descriptor, 1)
			} else {
				while (true) {
					when (val index = decodeElementIndex(descriptor)) {
						0 -> mostSignificationBits = decodeLongElement(descriptor, 0)
						1 -> leastSignificationBits = decodeLongElement(descriptor, 1)
						CompositeDecoder.DECODE_DONE -> break
						else -> error("Unexpected index: $index")
					}
				}
			}

			requireNotNull(mostSignificationBits)
			requireNotNull(leastSignificationBits)

			Uuid(mostSignificationBits, leastSignificationBits)
		}
	}
}

/**
 * String serializer for the Uuid object.
 */
public object UuidStringSerializer : KSerializer<Uuid> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("uuid", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: Uuid) {
		encoder.encodeString(value.toString())
	}

	override fun deserialize(decoder: Decoder): Uuid {
		return uuidFrom(decoder.decodeString())
	}
}

/**
 * String serializer for the old Minecraft Uuid object.
 */
public object OldUuidStringSerializer : KSerializer<Uuid> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("old_uuid", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): Uuid {
		val uuidWithoutHyphens = decoder.decodeString()
		return uuidFrom(
			"${uuidWithoutHyphens.substring(0, 8)}-${
				uuidWithoutHyphens.substring(
					8, 12
				)
			}-${uuidWithoutHyphens.substring(12, 16)}-${
				uuidWithoutHyphens.substring(
					16, 20
				)
			}-${uuidWithoutHyphens.substring(20)}"
		)
	}

	override fun serialize(encoder: Encoder, value: Uuid) {
		encoder.encodeString(value.toString().replace("-", ""))
	}
}