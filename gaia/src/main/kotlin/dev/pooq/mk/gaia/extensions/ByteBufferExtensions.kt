package dev.pooq.mk.gaia.extensions

import java.nio.ByteBuffer

const val SEGMENT_BITS = 0x7F
const val CONTINUE_BIT = 0x80

fun ByteBuffer.varInt(): Int {
  var value = 0
  var position = 0
  var currentByte: Byte

  while (true) {
    currentByte = get()
    value = value or (currentByte.toInt() and SEGMENT_BITS shl position)
    if (currentByte.toInt() and CONTINUE_BIT == 0) break
    position += 7
    if (position >= 32) throw RuntimeException("VarInt is too big")
  }

  return value
}

fun ByteBuffer.varLong(): Long {
  var value: Long = 0
  var position = 0
  var currentByte: Byte

  while (true) {
    currentByte = get()
    value = value or ((currentByte.toInt() and SEGMENT_BITS).toLong() shl position)
    if (currentByte.toInt() and CONTINUE_BIT == 0) break
    position += 7
    if (position >= 64) throw RuntimeException("VarLong is too big")
  }

  return value
}

fun ByteBuffer.varLong(long: Long){
  var value = long
  while (true) {
    if (value and 0x7FL.inv() == 0L) {
      put(value.toByte())
      return
    } else {
      put((value and 0x7F or 0x80).toByte())
      value = value ushr 7
    }
  }
}

fun ByteBuffer.varInt(int: Int){
  var value = int
  while (true) {
    if (value.toLong() and 0x7FL.inv() == 0L) {
      put(value.toByte())
      return
    } else {
      put((value and 0x7F or 0x80).toByte())
      value = value ushr 7
    }
  }
}

fun ByteBuffer.string(): String{
  return String(this.array())
}

fun ByteBuffer.string(string: String){
  this.put(string.toByteArray())
}