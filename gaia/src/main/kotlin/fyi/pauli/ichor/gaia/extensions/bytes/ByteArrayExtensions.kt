package fyi.pauli.ichor.gaia.extensions.bytes

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

fun ByteArray.compress(): ByteArray {
	val deflater = Deflater()
	deflater.setInput(this)
	deflater.finish()
	val buffer = ByteArray(4096)
	val stream = ByteArrayOutputStream(this.size)
	while (!deflater.finished()) {
		stream.write(buffer, 0, deflater.deflate(buffer))
	}
	stream.close()
	return stream.toByteArray()
}

fun ByteArray.decompress(length: Int? = null): ByteArray {
	val inflater = Inflater()
	inflater.setInput(this, 0, this.size)
	val buffer = ByteArray(4096)
	val stream = ByteArrayOutputStream(length ?: this.size)
	val maxSize = length ?: Int.MAX_VALUE
	while (!inflater.finished()) {
		if (stream.size() > maxSize) {
			throw OutOfMemoryError("Stream too large!")
		}
		stream.write(buffer, 0, inflater.inflate(buffer))
	}
	stream.close()
	return stream.toByteArray()
}
