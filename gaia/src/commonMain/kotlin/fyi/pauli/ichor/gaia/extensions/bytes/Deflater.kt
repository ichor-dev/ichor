package fyi.pauli.ichor.gaia.extensions.bytes

/**
 * @author btwonion
 * @since 21/11/2023
 */
internal expect object Compressor {

	fun compress(input: ByteArray): ByteArray

	fun decompress(input: ByteArray): ByteArray
}