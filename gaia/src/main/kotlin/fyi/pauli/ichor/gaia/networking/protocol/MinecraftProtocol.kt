package fyi.pauli.ichor.gaia.networking.protocol

import fyi.pauli.ichor.gaia.networking.protocol.serialization.MinecraftProtocolDecoder
import fyi.pauli.ichor.gaia.networking.protocol.serialization.MinecraftProtocolEncoder
import io.ktor.utils.io.*
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
 * 
 * This is a kotlinx.serialization format supporting the Minecraft protocol.
 * For using specific number encoding annotate the specific field with @NumberType.
 * When using enums you can use the @EnumSerial annotation to specify the number, which should be encoded.
 */
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