package fyi.pauli.ichor.gaia.entity.player

import com.benasher44.uuid.Uuid
import fyi.pauli.ichor.gaia.networking.serialization.OldUuidStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Userprofile of a connection.
 * @property uuid uuid of a connection.
 * @property username username of a connection-
 * @property properties list of properties of a connection.
 * @property profileActions list of actions of a connection.
 * @author Paul Kindler
 * @since 01/11/2023
 */
@Serializable
public data class UserProfile(
	@SerialName("id") val uuid: @Serializable(with = OldUuidStringSerializer::class) Uuid,
	@SerialName("name") val username: String,
	val properties: List<Property>,
	val profileActions: List<ProfileAction>
)

/**
 * Property data class used in the UserProfile.
 * @property name name of the property.
 * @property value value of the property.
 * @property signature signature of the property.
 * @author Paul Kindler
 * @since 01/11/2023
 */
@Serializable
public data class Property(val name: String, val value: String, val signature: String)

/**
 * TODO
 */
@Serializable
public class ProfileAction