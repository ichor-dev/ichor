package fyi.pauli.ichor.gaia.entity.player

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserProfile(
	val uuid: @Contextual UUID,
	val username: String,
	val properties: List<Property>
)

@Serializable
data class Property(val name: String, val value: String, val signature: String)