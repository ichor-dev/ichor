package fyi.pauli.ichor.gaia.networking.protocol.serialization

import fyi.pauli.ichor.gaia.networking.protocol.MinecraftInput
import fyi.pauli.ichor.gaia.networking.protocol.desc.ProtocolDesc
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractProtocolDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.findEnumIndexByTag
import fyi.pauli.ichor.gaia.networking.protocol.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftNumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftStringEncoder
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarIntSerializer.readVarInt
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarLongSerializer.readVarLong
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.internal.TaggedDecoder


/**
 * @author btwonion
 * @since 11/11/2023
 * 
 * Decoder for the Minecraft protocol format.
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
class MinecraftProtocolDecoder(private val input: MinecraftInput) : TaggedDecoder<ProtocolDesc>() {
	private var currentIndex = 0
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return if (descriptor.elementsCount == currentIndex) DECODE_DONE
		else currentIndex++
	}

	override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = runBlocking {
		readVarInt { input.readByte() }
	}

	override fun decodeSequentially(): Boolean {
		return true
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
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> input.readByte().toUByte().toByte()
			else -> input.readByte()
		}
	}

	override fun decodeTaggedShort(tag: ProtocolDesc): Short = runBlocking {
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> input.readShort().toUShort().toShort()
			else -> input.readShort()
		}
	}

	override fun decodeTaggedInt(tag: ProtocolDesc): Int = runBlocking {
		when (tag.type) {
			MinecraftNumberType.DEFAULT -> input.readInt()
			MinecraftNumberType.VAR -> readVarInt { input.readByte() }
			MinecraftNumberType.UNSIGNED -> input.readInt().toUInt().toInt()
		}
	}

	override fun decodeTaggedLong(tag: ProtocolDesc): Long = runBlocking {
		when (tag.type) {
			MinecraftNumberType.DEFAULT -> input.readLong()
			MinecraftNumberType.VAR -> readVarLong { input.readByte() }
			MinecraftNumberType.UNSIGNED -> input.readLong().toULong().toLong()
		}
	}

	override fun decodeTaggedFloat(tag: ProtocolDesc): Float = runBlocking {
		input.readFloat()
	}

	override fun decodeTaggedDouble(tag: ProtocolDesc): Double = runBlocking {
		input.readDouble()
	}

	@OptIn(ExperimentalStdlibApi::class)
	override fun decodeTaggedString(tag: ProtocolDesc): String = runBlocking {
		MinecraftStringEncoder.readString(readByte = { input.readByte() }) { length ->
			ByteArray(length) { input.readByte() }
		}
	}

	@OptIn(ExperimentalStdlibApi::class)
	override fun decodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor): Int = runBlocking {
		val enumTag = extractEnumDescriptor(enumDescriptor)
		val ordinal = when (enumTag.type) {
			MinecraftEnumType.VAR_INT -> readVarInt { input.readByte() }
			MinecraftEnumType.BYTE, MinecraftEnumType.UNSIGNED_BYTE -> input.readByte().toInt()
			MinecraftEnumType.INT -> input.readInt()
			MinecraftEnumType.STRING -> enumDescriptor.getElementIndex(MinecraftStringEncoder.readString(readByte = { input.readByte() }) { length ->
				ByteArray(length) { input.readByte() }
			})
		}

		findEnumIndexByTag(enumDescriptor, ordinal)
	}

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		return extractProtocolDescriptor(this, index)
	}

	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		return when (descriptor.kind) {
			StructureKind.CLASS, StructureKind.LIST -> MinecraftProtocolDecoder(input)
			else -> super.beginStructure(descriptor)
		}
	}
}