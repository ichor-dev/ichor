package fyi.pauli.ichor.gaia.tests

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.serialization.UserProfileSerializer
import fyi.pauli.ichor.gaia.networking.serialization.UuidLongSerializer
import fyi.pauli.nbterialize.extensions.buildCompoundTag
import fyi.pauli.nbterialize.serialization.types.CompoundTag
import fyi.pauli.prolialize.MinecraftProtocol
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author btwonion
 * @since 21/11/2023
 */
class ProtocolObjectTests {
	@Serializable
	data class Objects(
		val identifier: Identifier,
		val tag: CompoundTag,
		val uuid: @Serializable(with = UuidLongSerializer::class) Uuid,
		val userProfile: @Serializable(with = UserProfileSerializer::class) UserProfile
	)

	@Test
	fun checkObjectSerializing() {
		val objects = Objects(
			Identifier("ichor", "test"),
			buildCompoundTag {
				put("inttest", 1)
			},
			uuidFrom("84c7eef5-ae2c-4ebb-a006-c3ee07643d79"),
			UserProfile(
				uuidFrom("84c7eef5-ae2c-4ebb-a006-c3ee07643d79"),
				"btwonion",
				listOf(Property("ojojojoj", "jojoj", "signatur")),
				listOf()
			)
		)

		val mc = MinecraftProtocol()

		val encoded = mc.decodeFromByteArray<Objects>(mc.encodeToByteArray(objects))

		assertEquals(objects, encoded)
	}
}