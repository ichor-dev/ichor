package fyi.pauli.ichor.gaia.extensions.bytes

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

fun ByteArray.compress(): Int {
	val compressor = Deflater()
	compressor.setInput(this@compress)
	compressor.finish()
	return compressor.deflate(this@compress)
}

fun ByteArray.decompress(length: Int): ByteArray {
	val inflater = Inflater()
	val outputStream = ByteArrayOutputStream()

	return outputStream.use {
		val buffer = ByteArray(length)

		inflater.setInput(this)

		var count = -1
		while (count != 0) {
			count = inflater.inflate(buffer)
			outputStream.write(buffer, 0, count)
		}

		inflater.end()
		outputStream.toByteArray()
	}
}
