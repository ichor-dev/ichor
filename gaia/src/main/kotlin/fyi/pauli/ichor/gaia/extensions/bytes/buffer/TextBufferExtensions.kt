package fyi.pauli.ichor.gaia.extensions.bytes.buffer

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

fun ByteBuffer.string(value: String) {
	val bytes = value.toByteArray(StandardCharsets.UTF_8)
	varInt(bytes.size)
	rawBytes(bytes)
}

fun ByteBuffer.string(): String {
	val length = varInt()
	return String(rawBytes(length), StandardCharsets.UTF_8)
}