package fyi.pauli.ichor.gaia.extensions.bytes

import com.oldguy.common.io.CompressionDeflate
import fyi.pauli.prolialize.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.prolialize.exceptions.MinecraftProtocolEncodingException

internal suspend fun ByteArray.compress(): ByteArray {
	var byteArray: ByteArray? = null
	CompressionDeflate(false).compressArray({ this }) {
		byteArray = it
	}
	return byteArray ?: throw MinecraftProtocolEncodingException("deflater failed!")
}

internal suspend fun ByteArray.decompress(): ByteArray {
	var byteArray: ByteArray? = null
	CompressionDeflate(false).decompressArray({ this }) {
		byteArray = it
	}
	return byteArray ?: throw MinecraftProtocolDecodingException("deflater failed!")
}
