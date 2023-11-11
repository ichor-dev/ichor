package fyi.pauli.ichor.gaia.networking.protocol

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
		TODO()
	}

	override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		val packetBuilder = BytePacketBuilder()

		return packetBuilder.build().readBytes()
	}
}

internal typealias MinecraftOutput = ByteWriteChannel
internal typealias MinecraftInput = ByteReadChannel