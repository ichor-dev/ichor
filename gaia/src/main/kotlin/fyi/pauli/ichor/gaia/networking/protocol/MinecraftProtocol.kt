package fyi.pauli.ichor.gaia.networking.protocol

import fyi.pauli.ichor.gaia.networking.protocol.serialization.MinecraftProtocolDecoder
import fyi.pauli.ichor.gaia.networking.protocol.serialization.MinecraftProtocolEncoder
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * @author btwonion
 * @since 11/11/2023
 */
internal val protocolScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

class MinecraftProtocol(
	override val serializersModule: SerializersModule = EmptySerializersModule()
) : BinaryFormat {

	@InternalSerializationApi
	override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		val decoder = MinecraftProtocolDecoder(ByteReadChannel(bytes))

		return decoder.decodeSerializableValue(deserializer)
	}

	override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		val channel = ByteChannel(autoFlush = false)
		val encoder = MinecraftProtocolEncoder(channel)
		encoder.encodeSerializableValue(serializer, value)
		channel.flush()

		return ByteArray(channel.availableForRead) { runBlocking { channel.readByte() } }
	}
}

internal typealias MinecraftOutput = ByteWriteChannel
internal typealias MinecraftInput = ByteReadChannel