package dev.pooq.ichor.gaia.tests

import dev.pooq.ichor.gaia.extensions.bytes.short
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.extensions.bytes.varLong
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteBufferExtensionTests {

	@Test
	fun `read and write string`() {
		val expected = "Hokus Pokus fidupus"

		val buffer = ByteBuffer.allocate(expected.length)
		buffer.string(expected)

		val found = buffer.flip().string()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write int`() {
		val expected = 69

		val buffer = ByteBuffer.allocate(expected)
		buffer.varInt(expected)

		val found = buffer.flip().varInt()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write long`() {
		val expected = 420L

		val buffer = ByteBuffer.allocate(expected.toInt())
		buffer.varLong(expected)

		val found = buffer.flip().varLong()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write short`() {
		val expected = Short.MAX_VALUE

		val buffer = ByteBuffer.allocate(expected.toInt())
		buffer.short(expected)

		val found = buffer.flip().short()

		assertEquals(expected, found)
	}
}