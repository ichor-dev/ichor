package fyi.pauli.ichor.gaia.networking.serialization

import fyi.pauli.ichor.gaia.models.Identifier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @author btwonion
 * @since 15/11/2023
 *
 * Byte serializer for the ichor internal Identifier object.
 */
object IdentifierByteSerializer : KSerializer<Identifier> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("identifier", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): Identifier {
		val (namespace, value) = decoder.decodeString().split(":")
		return Identifier(namespace, value)
	}

	override fun serialize(encoder: Encoder, value: Identifier) {
		encoder.encodeString(value.toString())
	}
}