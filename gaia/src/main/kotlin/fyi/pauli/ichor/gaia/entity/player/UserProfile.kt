package fyi.pauli.ichor.gaia.entity.player

import fyi.pauli.ichor.gaia.extensions.serializers.OldUUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserProfile(
	@SerialName("id") val uuid: @Serializable(with = OldUUIDSerializer::class) UUID,
	@SerialName("name") val username: String,
	val properties: List<Property>,
	val profileActions: List<ProfileAction>
)

@Serializable
data class Property(val name: String, val value: String, val signature: String)

@Serializable
class	ProfileAction