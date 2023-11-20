package fyi.pauli.ichor.gaia.networking.serialization

import com.benasher44.uuid.Uuid
import fyi.pauli.ichor.gaia.entity.player.ProfileAction
import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

/**
 * @author btwonion
 * @since 20/11/2023
 *
 * Serializer for the Ichor internal UserProfile object.
 */
public object UserProfileSerializer : KSerializer<UserProfile> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("userprofile") {
		element<Uuid>("uuid")
		element<String>("username")
		element<List<Property>>("properties")
		element<List<ProfileAction>>("profileActions")
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): UserProfile {
		return decoder.decodeStructure(descriptor) {
			var uuid: Uuid? = null
			var username: String? = null
			var properties: List<Property>? = null
			var profileActions: List<ProfileAction>? = null

			if (decodeSequentially()) {
				uuid = decodeSerializableElement(descriptor, 0, UuidLongSerializer)
				username = decodeStringElement(descriptor, 1)
				properties = decodeSerializableElement(descriptor, 2, ListSerializer(Property.serializer()))
				profileActions = decodeSerializableElement(descriptor, 3, ListSerializer(ProfileAction.serializer()))
			} else {
				while (true) {
					when (val index = decodeElementIndex(UuidLongSerializer.descriptor)) {
						0 -> uuid = decodeSerializableElement(descriptor, 0, UuidLongSerializer)
						1 -> username = decodeStringElement(descriptor, 1)
						2 -> properties = decodeSerializableElement(descriptor, 2, ListSerializer(Property.serializer()))
						3 -> profileActions = decodeSerializableElement(descriptor, 3, ListSerializer(ProfileAction.serializer()))
						CompositeDecoder.DECODE_DONE -> break
						else -> error("Unexpected index: $index")
					}
				}
			}

			requireNotNull(uuid)
			requireNotNull(username)
			requireNotNull(properties)
			requireNotNull(profileActions)

			UserProfile(uuid, username, properties, profileActions)
		}
	}

	override fun serialize(encoder: Encoder, value: UserProfile) {
		encoder.encodeStructure(descriptor) {
			encodeSerializableElement(descriptor, 0, UuidLongSerializer, value.uuid)
			encodeStringElement(descriptor, 1, value.username)
			encodeSerializableElement(descriptor, 2, ListSerializer(Property.serializer()), value.properties)
			encodeSerializableElement(descriptor, 3, ListSerializer(ProfileAction.serializer()), value.profileActions)
		}
	}

}