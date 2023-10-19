package fyi.pauli.ichor.gaia.extensions.bytes.buffer

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

fun ByteBuffer.boolean(value: Boolean) {
	put(if (value) 1 else 0)
}

fun ByteBuffer.boolean(): Boolean {
	return get() == 1.toByte()
}

fun ByteBuffer.byte(value: Byte) {
	put(value)
}

fun ByteBuffer.byte(): Byte {
	return get()
}

fun ByteBuffer.byteArray(value: ByteArray) {
	varInt(value.size)
	rawBytes(value)
}

fun ByteBuffer.byteArray(): ByteArray {
	val length = varInt()
	return rawBytes(length)
}

fun ByteBuffer.rawBytes(length: Int): ByteArray {
	val bytes = ByteArray(length)
	get(bytes)
	return bytes
}

fun ByteBuffer.rawBytes(value: ByteArray) {
	put(value)
}

fun ByteBuffer.rawBytes(): ByteArray? {
	val length: Int = limit() - position()
	if (length <= 0) return null
	val bytes = ByteArray(length)
	get(bytes)
	return bytes
}

fun ByteBuffer.short(value: Short) {
	putShort(value)
}

fun ByteBuffer.short(): Short {
	return getShort()
}

fun ByteBuffer.int(value: Int) {
	putInt(value)
}

fun ByteBuffer.int(): Int {
	return getInt()
}

fun ByteBuffer.intArray(value: IntArray) {
	varInt(value.size)
	value.forEach { int(it) }
}

fun ByteBuffer.intArray(): IntArray {
	return IntArray(varInt()) { int() }
}

fun ByteBuffer.long(value: Long) {
	putLong(value)
}

fun ByteBuffer.long(): Long {
	return getLong()
}

fun ByteBuffer.longArray(value: LongArray) {
	varInt(value.size)
	value.forEach { long(it) }
}

fun ByteBuffer.longArray(): LongArray {
	return LongArray(varInt()) { long() }
}

fun ByteBuffer.float(value: Float) {
	putFloat(value)
}

fun ByteBuffer.float(): Float {
	return getFloat()
}

fun ByteBuffer.double(value: Double) {
	putDouble(value)
}

fun ByteBuffer.double(): Double {
	return getDouble()
}

fun ByteBuffer.string(value: String) {
	val bytes = value.toByteArray(StandardCharsets.UTF_8)
	varInt(bytes.size)
	rawBytes(bytes)
}

fun ByteBuffer.string(): String {
	val length = varInt()
	return String(rawBytes(length), StandardCharsets.UTF_8)
}