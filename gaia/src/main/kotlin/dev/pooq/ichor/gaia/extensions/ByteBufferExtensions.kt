package dev.pooq.ichor.gaia.extensions

import java.nio.ByteBuffer

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
  var remainingValue = long
  while (remainingValue and SEGMENT_BITS.toLong().inv() != 0L) {
    this.put((remainingValue and SEGMENT_BITS.toLong()).or(CONTINUE_BIT.toLong()).toByte())
    remainingValue = remainingValue ushr 7
  }
  this.put(remainingValue.toByte())
}

fun ByteBuffer.varInt(int: Int) {
  var remainingValue = int
  while (remainingValue and 0xFFFFFF80.toInt() != 0) {
    this.put(((remainingValue and SEGMENT_BITS) or CONTINUE_BIT).toByte())
    remainingValue = remainingValue ushr 7
  }
  this.put(remainingValue.toByte())
}

fun ByteBuffer.string(string: String) {
  val maxLength = Short.MAX_VALUE

  if (string.length > maxLength) throw IllegalArgumentException("String is too long.")

  val bytes = string.toByteArray(Charsets.UTF_8)

  if (bytes.size > (maxLength * 4) + 3) throw throw IllegalArgumentException("String bytearray is too long.")

  put(bytes)
}

fun ByteBuffer.short(short: Short) {
  this.put((short.toInt() ushr 8 and 0xFF).toByte())
  this.put((short.toInt() and 0xFF).toByte())
}

fun ByteBuffer.byteArray(length: Int) = ByteArray(length).also(this::get)