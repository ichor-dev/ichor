package fyi.pauli.ichor.gaia.networking.protocol.serialization

import fyi.pauli.ichor.gaia.networking.protocol.MinecraftInput
import fyi.pauli.ichor.gaia.networking.protocol.desc.ProtocolDesc
import fyi.pauli.ichor.gaia.networking.protocol.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.ichor.gaia.networking.protocol.protocolScope
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftNumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.VarIntEncoder.readVarInt
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.internal.TaggedDecoder

/**
 * @author btwonion
 * @since 11/11/2023
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
class MinecraftProtocolDecoder(private val input: MinecraftInput) : TaggedDecoder<ProtocolDesc>() {
	private var currentIndex = 0
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return if (descriptor.elementsCount == currentIndex) DECODE_DONE
		else currentIndex++
	}

	override fun decodeTaggedBoolean(
		tag: ProtocolDesc
	): Boolean = runBlocking {
		when (val i = input.readByte()) {
			0x00.toByte() -> false
			0x01.toByte() -> true
			else -> throw MinecraftProtocolDecodingException("Expected boolean value (0 or 1), found $i")
		}
	}

	override fun decodeTaggedByte(tag: ProtocolDesc): Byte = runBlocking {
		input.readByte()
	}

	override fun decodeTaggedShort(tag: ProtocolDesc): Short = runBlocking {
		input.readShort()
	}

	override fun decodeTaggedInt(tag: ProtocolDesc): Int = runBlocking {
		when (tag.type) {
			MinecraftNumberType.DEFAULT, MinecraftNumberType.UNSIGNED -> input.readInt()
			MinecraftNumberType.VAR -> readVarInt { input.readByte() }
		}
	}

	override fun decodeTaggedLong(tag: ProtocolDesc): Long = runBlocking {
		when (tag.type) { // TODO: impl VarLong?
			MinecraftNumberType.UNSIGNED, MinecraftNumberType.DEFAULT -> input.readLong()
			MinecraftNumberType.VAR ->
		}
	}

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		TODO("Not yet implemented")
	}
}