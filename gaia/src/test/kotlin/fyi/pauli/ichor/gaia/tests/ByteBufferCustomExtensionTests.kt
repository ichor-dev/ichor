package fyi.pauli.ichor.gaia.tests

import fyi.pauli.ichor.gaia.entity.player.ProfileAction
import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.*
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.models.nbt.builder.compoundTag
import fyi.pauli.ichor.gaia.networking.VAR_INT
import java.nio.ByteBuffer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteBufferCustomExtensionTests {
	@Test
	fun `read and write identifier`() {
		val expected = Identifier("ichor", "serializationtest")
		val buffer = ByteBuffer.allocate(expected.toString().toByteArray().size + VAR_INT)
		buffer.identifier(expected)

		val found = buffer.flip().identifier()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write compound tag`() {
		val expected = compoundTag("oka") {
			put("test1", 4)
			put("asda", "asdawd")
		}
		val buffer = ByteBuffer.allocate(1024)
		buffer.compoundTag(expected)

		val found = buffer.flip().compoundTag()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write user profile`() {
		val expected = UserProfile(UUID.randomUUID(), "onionionion", listOf(Property("name", "value", "signature")), listOf(ProfileAction()))
		val buffer = ByteBuffer.allocate(1024)
		buffer.userProfile(expected)

		val found = buffer.flip().userProfile()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write uuid`() {
		val expected = UUID.randomUUID()
		val buffer = ByteBuffer.allocate(1024)
		buffer.uuid(expected)

		val found = buffer.flip().uuid()

		assertEquals(expected, found)
	}
}