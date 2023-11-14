package fyi.pauli.ichor.gaia.networking.protocol.serialization.types.objects

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.UUID

/**
 * @author btwonion
 * @since 14/11/2023
 */
object UuidByteSerializer : KSerializer<UUID> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("UUID") {
		element<Long>("msb")
		element<Long>("lsb")
	}

	override fun serialize(encoder: Encoder, value: UUID) {
		encoder.encodeStructure(descriptor) {
			encodeLongElement(descriptor, 0, value.mostSignificantBits)
			encodeLongElement(descriptor, 1, value.leastSignificantBits)
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): UUID = decoder.decodeStructure(descriptor) {
		var mostSignificationBits: Long? = null
		var leastSignificationBits: Long? = null

		if (decodeSequentially()) { // sequential decoding protocol
			mostSignificationBits = decodeLongElement(descriptor, 0)
			leastSignificationBits = decodeLongElement(descriptor, 1)
		} else while (true) {
			when (val index = decodeElementIndex(descriptor)) {
				0 -> mostSignificationBits = decodeLongElement(descriptor, 0)
				1 -> leastSignificationBits = decodeLongElement(descriptor, 1)
				CompositeDecoder.DECODE_DONE -> break
				else -> error("Unexpected index: $index")
			}
		}

		requireNotNull(mostSignificationBits)
		requireNotNull(leastSignificationBits)

		UUID(mostSignificationBits, leastSignificationBits)
	}
}

object UuidStringSerializer : KSerializer<UUID> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("uuid", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: UUID) {
		encoder.encodeString(value.toString())
	}

	override fun deserialize(decoder: Decoder): UUID {
		return UUID.fromString(decoder.decodeString())
	}
}

object OldUuidStringSerializer : KSerializer<UUID> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("old_uuid", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): UUID {
		val uuidWithoutHyphens = decoder.decodeString()
		return UUID.fromString(
			uuidWithoutHyphens.substring(0, 8) + "-" +
				uuidWithoutHyphens.substring(8, 12) + "-" +
				uuidWithoutHyphens.substring(12, 16) + "-" +
				uuidWithoutHyphens.substring(16, 20) + "-" +
				uuidWithoutHyphens.substring(20)
		)
	}

	override fun serialize(encoder: Encoder, value: UUID) {
		encoder.encodeString(value.toString().replace("-", ""))
	}
}