package dev.pooq.mk.gaia.extensions

import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import kotlin.experimental.and

const val SEGMENT_BITS = 0x7F
const val CONTINUE_BIT = 0x80

fun ByteBuffer.varInt(): Int{
  var value = 0
  var position = 0
  var currentByte: Byte

  while (true){
    currentByte = this.flip().get()
    value = (currentByte and SEGMENT_BITS.toByte()).toInt() shl position
    if((currentByte.toInt() and CONTINUE_BIT) == 0) break

    position += 7

    if(position >= 32) throw RuntimeException("VarInt is too big.")
  }

  return value
}

fun ByteBuffer.varLong(): Long{
  var value: Long = 0
  var position = 0
  var currentByte: Byte

  while (true){
    currentByte = this.flip().get()
    value = value or ((currentByte.toInt() and SEGMENT_BITS).toLong()) shl position
    if((currentByte.toInt() and CONTINUE_BIT) == 0) break

    position += 7

    if(position >= 64) throw RuntimeException("VarInt is too big.")
  }

  return value
}

fun ByteBuffer.varLong(long: Long){
  var value = long
  while (true){
    if(value and SEGMENT_BITS.toLong().inv() == 0L){
      this.putLong(value)
      return
    }

    this.putLong(value and SEGMENT_BITS.toLong() or CONTINUE_BIT.toLong())
    value = value ushr 7
  }
}

fun ByteBuffer.varInt(int: Int){
  var value = int
  while (true){
    if(value and SEGMENT_BITS.inv() == 0){
      this.putInt(value)
      return
    }

    this.putInt(value and SEGMENT_BITS or CONTINUE_BIT)
    value = value ushr 7
  }
}

fun ByteBuffer.string(): String{
  return StandardCharsets.UTF_8.decode(this.flip()).toString()
}

fun ByteBuffer.string(string: String){
  this.asCharBuffer().put(string)
}