package dev.pooq.ichor.gaia.tests

import dev.pooq.ichor.gaia.extensions.compress
import dev.pooq.ichor.gaia.extensions.decompress
import kotlin.test.Test

class ByteArrayExtensionTests {

  @Test
  fun compressAndDecompress(){
    val original = "My name is Paul"

    val array = original.toByteArray()

    val compressed = array.compress()

    val decompressed = compressed.decompress(original.length)

    val equals = array.contentEquals(decompressed)

    assert(equals)
  }
}