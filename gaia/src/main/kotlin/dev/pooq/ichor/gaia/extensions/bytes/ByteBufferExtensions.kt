package dev.pooq.ichor.gaia.extensions.bytes

import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import java.nio.ByteBuffer
import java.util.*


private const val SEGMENT_BITS = 0x7F
private const val SEGMENT_BITS_LONG = (0x7F).toLong()
private const val CONTINUE_BIT = 0x80
private const val CONTINUE_BIT_LONG = (0x80).toLong()

inline fun ServerPacket.uncompressedBuffer(length: Int, applier: ByteBuffer.() -> Unit = {}): ByteBuffer =
  ByteBuffer.allocate(INT + INT + length).apply {
    varInt(INT + length)
    varInt(id)
  }.apply(applier)

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
    if (position >= 64) throw java.lang.RuntimeException("VarLong is too big")
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

fun ByteBuffer.uuid(): UUID {
  return UUID(varLong(), varLong())
}

fun ByteBuffer.varLong(value: Long) {
  var remainingValue = value
  while (true) {
    if ((remainingValue and SEGMENT_BITS_LONG.inv()) == 0L) {
      this.put(remainingValue.toByte())
      return
    }

    val byteToWrite = (remainingValue and SEGMENT_BITS_LONG) or CONTINUE_BIT_LONG
    this.put(byteToWrite.toByte())

    remainingValue = remainingValue ushr 7
  }
}

fun ByteBuffer.varInt(value: Int) {
  var remainingValue = value
  while (true) {
    if ((remainingValue and SEGMENT_BITS.inv()) == 0) {
      this.put(remainingValue.toByte())
      return
    }

    val byteToWrite = (remainingValue and SEGMENT_BITS) or CONTINUE_BIT
    this.put(byteToWrite.toByte())

    remainingValue = remainingValue ushr 7
  }
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