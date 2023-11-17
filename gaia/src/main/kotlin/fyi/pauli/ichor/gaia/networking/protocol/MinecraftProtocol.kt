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

	/**
	 * This function is used to decode a serializable object from a Minecraft protocol formatted ByteArray.
	 * @param deserializer specifies the serialization stragegy to decode the object, by default MyObject.serializer()
	 * @param bytes the input ByteArray the object should be read from
	 * @returns the decoded object
	 */
	@InternalSerializationApi
	override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		val decoder = MinecraftProtocolDecoder(ByteReadChannel(bytes))

		return decoder.decodeSerializableValue(deserializer)
	}

	/**
	 * This function is used to encode a serializable object to a Minecraft protocol formatted ByteArray.
	 * @param serializer specifies the serialization stragegy to encode the object, by default MyObject.serializer()
	 * @param value the object, which should be transformed to a ByteArray
	 * @returns the encoded ByteArray
	 */
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