package dev.pooq.ichor.gaia.entity.player

import dev.pooq.ichor.gaia.extensions.serializers.OldUUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
data class UserProfile(
	val id: @Serializable(with = OldUUIDSerializer::class) UUID,
	val name: String,
	val properties: List<Property>
)

@Serializable
data class Property(val name: String, val value: String, val signature: String)