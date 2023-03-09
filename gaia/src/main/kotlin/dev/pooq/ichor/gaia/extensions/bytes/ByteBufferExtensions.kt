package dev.pooq.ichor.gaia.extensions.bytes

import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and

private const val SEGMENT_BITS = 0x7F
private const val CONTINUE_BIT = 0x80

inline fun buffer(capacity: Int, applier: ByteBuffer.() -> Unit = {}): ByteBuffer =
  ByteBuffer.allocate(capacity).apply(applier).flip()

inline fun ServerPacket.uncompressedBuffer(length: Int, applier: ByteBuffer.() -> Unit = {}) =
  ByteBuffer.allocate(
    INT + INT + length
  ).apply {
    varInt(length + INT)
    varInt(id)
  }.apply(applier)

fun ByteBuffer.varInt(): Int {
  var result = 0
  var shift = 0
  while (shift < 32) {
    val b = get()
    result = result or ((b.toInt() and SEGMENT_BITS) shl shift)
    if ((b and CONTINUE_BIT.toByte()) == 0.toByte()) {
      return result
    }
    shift += 7
  }
  throw IllegalArgumentException("Malformed VarInt")
}

fun ByteBuffer.varLong(): Long {
  var result: Long = 0
  var shift = 0
  var b: Byte
  do {
    b = this.get()
    result = result or ((b.toLong() and SEGMENT_BITS.toLong()) shl shift)
    shift += 7
  } while (b < 0)
  return result
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

fun ByteBuffer.uuid(): UUID {
  return UUID(varLong(), varLong())
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
  val encoded = string.toByteArray(Charsets.UTF_8)
  varInt(encoded.size)
  put(encoded)
}

fun ByteBuffer.short(short: Short) {
  this.put((short.toInt() ushr 8 and 0xFF).toByte())
  this.put((short.toInt() and 0xFF).toByte())
}

fun ByteBuffer.byteArray(length: Int) = ByteArray(length).also(this::get)