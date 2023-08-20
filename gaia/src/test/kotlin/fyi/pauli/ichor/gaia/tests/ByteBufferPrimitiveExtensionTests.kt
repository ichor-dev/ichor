package fyi.pauli.ichor.gaia.tests

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.*
import fyi.pauli.ichor.gaia.networking.INT
import fyi.pauli.ichor.gaia.networking.VAR_INT
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ByteBufferPrimitiveExtensionTests {

	@Test
	fun `read and write string`() {
		val expected = "Hokus Pokus fidupus"

		val buffer = ByteBuffer.allocate(expected.length + VAR_INT)
		buffer.string(expected)

		val found = buffer.flip().string()

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

	@Test
	fun `read and write boolean`() {
		val expected = true
		val buffer = ByteBuffer.allocate(1)
		buffer.boolean(expected)

		val found = buffer.flip().boolean()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write byte array`() {
		val expected = "sdasd asdasd".toByteArray()
		val buffer = ByteBuffer.allocate(expected.size + VAR_INT)
		buffer.byteArray(expected)

		val found = buffer.flip().byteArray()

		assertContentEquals(expected, found)
	}

	@Test
	fun `read and write int array`() {
		val expected = (2..5).toList().toIntArray()
		val buffer = ByteBuffer.allocate((expected.size + 1) * INT + VAR_INT)
		buffer.intArray(expected)

		val found = buffer.flip().intArray()

		assertContentEquals(expected, found)
	}
}