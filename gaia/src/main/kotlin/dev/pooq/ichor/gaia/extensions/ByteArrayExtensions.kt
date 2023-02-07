package dev.pooq.ichor.gaia.extensions

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

fun ByteArray.compress(): ByteArray{
  val output = ByteArray(this.size * 4)
  val compressor = Deflater().apply {
    setInput(this@compress)
    finish()
  }
  val compressedDataLength: Int = compressor.deflate(output)
  return output.copyOfRange(0, compressedDataLength)
}

fun ByteArray.decompress(length: Int): ByteArray{
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
