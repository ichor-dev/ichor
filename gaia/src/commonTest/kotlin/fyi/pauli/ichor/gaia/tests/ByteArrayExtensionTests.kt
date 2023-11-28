package fyi.pauli.ichor.gaia.tests

import fyi.pauli.ichor.gaia.extensions.bytes.Compressor
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class ByteArrayExtensionTests {

	@Test
	fun compressAndDecompress() = runBlocking {
		val array = "My name is Paul".toByteArray()

		val compressed = Compressor.compress(array)
		val decompressed = Compressor.decompress(compressed)

		val equals = array.contentEquals(decompressed)

		assertTrue(equals)
	}
}