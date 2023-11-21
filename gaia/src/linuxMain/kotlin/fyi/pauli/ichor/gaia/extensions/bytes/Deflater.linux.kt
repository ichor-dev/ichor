package fyi.pauli.ichor.gaia.extensions.bytes

import kotlinx.cinterop.*
import platform.posix.memset
import platform.posix.size_tVar
import platform.zlib.*
/**
 * @author btwonion
 * @since 21/11/2023
 */
internal actual object Compressor {
	private var crc: Int = 0
	private var finished: Boolean = false
	private var finish: Int = Z_FINISH

	@OptIn(ExperimentalForeignApi::class)
	actual fun compress(input: ByteArray): ByteArray {
		memScoped {
			// Allocate output buffer with maximum possible size
			val maxOutputSize = compressBound(input.size.toULong()).toInt()
			val output = ByteArray(maxOutputSize)

			// Prepare input and output pointers
			val inputPtr = input.refTo(0).getPointer(this)
			val outputPtr = output.refTo(0).getPointer(this)
			val outputSizePtr = nativeHeap.alloc<size_tVar>()

			// Initialize the deflate stream
			val stream = alloc<z_stream>()
			// Clear the structure
			memset(stream.ptr, 0, sizeOf<z_stream>().convert())

			deflateInit(stream.ptr, Z_DEFAULT_COMPRESSION)
			stream.next_in = inputPtr.reinterpret()
			stream.avail_in = input.size.toUInt()
			stream.next_out = outputPtr.reinterpret()
			stream.avail_out = output.size.toUInt()

			// Compress the data
			crc = deflate(stream.ptr, finish)
			check(crc != Z_STREAM_ERROR) { "Failed to compress data" }
			// Update output buffer size
			outputSizePtr.value = (output.size - stream.avail_out.toInt()).toULong()
			// Finalize the deflate stream
			check(deflateEnd(stream.ptr) == Z_OK) { "Failed to finalize compression" }
			finished = true
			// Create a new byte array with the compressed data
			return output.copyOf(outputSizePtr.value.toInt())
		}
	}

	@OptIn(ExperimentalForeignApi::class)
	@Suppress("unchecked_cast")
	actual fun decompress(input: ByteArray): ByteArray {
		memScoped {

			/* Create a zlib stream structure */
			val stream = alloc<z_stream>()

			/* Initialize the zlib stream */
			stream.next_in = input.refTo(0).getPointer(this) as CPointer<uByteVar>
			stream.avail_in = input.size.toUInt()

			/* Specify the decompression mode */
			inflateInit(stream.ptr)

			val outputBufferLength = 4096
			val outputBuffer = ByteArray(outputBufferLength)
			var decompressedData = ByteArray(0)

			try {
				while (true) {

					/* Set the output buffer and its length */
					stream.next_out = outputBuffer.refTo(0).getPointer(this) as CPointer<uByteVar>
					stream.avail_out = outputBufferLength.toUInt()

					/* Decompress the data */
					val result = inflate(stream.ptr, Z_NO_FLUSH)

					when (result) {

						Z_STREAM_END -> {
							/* The end of the compressed data was reached */
							val bytesWritten = outputBufferLength - stream.avail_out.toInt()
							decompressedData += outputBuffer.take(bytesWritten)
							break
						}

						Z_OK -> {
							/* More decompressed data is available */
							val bytesWritten = outputBufferLength - stream.avail_out.toInt()
							decompressedData += outputBuffer.take(bytesWritten)
						}

						else -> {
							/* An error occurred during decompression */
							inflateEnd(stream.ptr)
							throw RuntimeException("Decompression error: $result")
						}
					}
				}

			} finally {
				/* Clean up the zlib stream */
				inflateEnd(stream.ptr)
			}

			return decompressedData
		}
	}
}