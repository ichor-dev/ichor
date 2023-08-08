package fyi.pauli.ichor.gaia.entity.player

import fyi.pauli.ichor.gaia.extensions.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserProfile(
	val id: @Serializable(with = UUIDSerializer::class) UUID,
	val name: String,
	val properties: List<Property>
)

@Serializable
data class Property(val name: String, val value: String, val signature: String)