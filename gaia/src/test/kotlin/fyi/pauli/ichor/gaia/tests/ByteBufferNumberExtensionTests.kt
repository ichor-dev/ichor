package fyi.pauli.ichor.gaia.tests

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.*
import fyi.pauli.ichor.gaia.networking.VAR_INT
import fyi.pauli.ichor.gaia.networking.VAR_LONG
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ByteBufferNumberExtensionTests {

	@Test
	fun `read and write short`() {
		val expected = Short.MAX_VALUE

		val buffer = ByteBuffer.allocate(expected.toInt())
		buffer.unsignedShort(expected)

		val found = buffer.flip().unsignedShort()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write var int`() {
		val expected = 69

		val buffer = ByteBuffer.allocate(expected)
		buffer.varInt(expected)

		val found = buffer.flip().varInt()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write var long`() {
		val expected = 420L

		val buffer = ByteBuffer.allocate(expected.toInt())
		buffer.varLong(expected)

		val found = buffer.flip().varLong()

		assertEquals(expected, found)
	}

	@Test
	fun `read and write var int array`() {
		val expected = (2..5).toList().toIntArray()
		val buffer = ByteBuffer.allocate(expected.size + VAR_INT)
		buffer.varIntArray(expected)

		val found = buffer.flip().varIntArray()

		assertContentEquals(expected, found)
	}

	@Test
	fun `read and write var long array`() {
		val expected = (2L..5L).toList().toLongArray()
		val buffer = ByteBuffer.allocate(expected.size + 1 * VAR_LONG + VAR_INT)
		buffer.varLongArray(expected)

		val found = buffer.flip().varLongArray()

		assertContentEquals(expected, found)
	}
}