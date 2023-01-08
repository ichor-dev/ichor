package dev.pooq.mk.gaia.tests

import dev.pooq.mk.gaia.extensions.string
import dev.pooq.mk.gaia.extensions.varInt
import dev.pooq.mk.gaia.extensions.varLong
import java.nio.ByteBuffer
import kotlin.math.sign
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteBufferExtensionTests {

  @Test
  fun `read and write string`(){
    val expected = "Hello world"

    val buffer = ByteBuffer.allocate(expected.length)
    buffer.string(expected)

    val found = buffer.string()

    assertEquals(expected, found)
  }

  @Test
  fun `read and write int`(){
    val expected = 10

    val buffer = ByteBuffer.allocate(expected)
    buffer.varInt(expected)

    val found = buffer.varInt()

    assertEquals(expected, found)
  }

  @Test
  fun `read and write long`(){
    val expected = 10L

    val buffer = ByteBuffer.allocate(expected.toInt())
    buffer.varLong(expected)

    val found = buffer.varLong()

    assertEquals(expected, found)
  }
}