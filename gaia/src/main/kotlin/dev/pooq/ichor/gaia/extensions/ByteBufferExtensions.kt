package dev.pooq.ichor.gaia.extensions

import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

private const val SEGMENT_BITS = 0x7F
private const val CONTINUE_BIT = 0x80

inline fun buffer(capacity: Int, applier: ByteBuffer.() -> Unit = {}): ByteBuffer =
  ByteBuffer.allocate(capacity).apply(applier).flip()

fun ByteBuffer.varInt(): Int {
  var value = 0
  var position = 0
  var currentByte: Byte

  while (true) {
    currentByte = get()
    value = value or ((currentByte.toInt() and SEGMENT_BITS) shl position)

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
    value = value or (((currentByte.toInt() and SEGMENT_BITS) shl position).toLong())

    if (currentByte.toInt() and CONTINUE_BIT == 0) break

    position += 7

    if (position >= 64) throw RuntimeException("VarLong is too big")
  }

  return value
}

fun ByteBuffer.boolean(): Boolean {
  return get().toInt() == 0x01
}

fun ByteBuffer.boolean(boolean: Boolean) {
  put(if (boolean) 1 else 0)
}

fun ByteBuffer.string(): String {
  val length = varInt()
  val bytes = ByteArray(length)

  get(bytes)

  return String(bytes, Charsets.UTF_8)
}


fun ByteBuffer.short(): Short {
  return (get().toInt() and 0xFF shl 8 or (get().toInt() and 0xFF)).toShort()
}

fun ByteBuffer.varLong(long: Long) {
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

fun ByteBuffer.varInt(int: Int) {
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

fun ByteBuffer.string(string: String) {
  val maxLength = Short.MAX_VALUE

  if (string.length > maxLength) throw IllegalArgumentException("String is too long.")

  val bytes = string.toByteArray(Charsets.UTF_8)

  if (bytes.size > (maxLength * 4) + 3) throw throw IllegalArgumentException("String bytearray is too long.")

  put(bytes)
}

fun ByteBuffer.short(short: Short) {
  var value = short
  while (true) {
    if (value.toLong() and 0x7FL.inv() == 0L) {
      put(value.toByte())
      return
    } else {
      put((value and 0x7F or 0x80).toByte())
      value = (value.toInt() ushr 7).toShort()
    }
  }
}
