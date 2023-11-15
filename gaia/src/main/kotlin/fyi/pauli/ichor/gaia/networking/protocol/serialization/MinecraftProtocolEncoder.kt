package fyi.pauli.ichor.gaia.networking.protocol.serialization

import fyi.pauli.ichor.gaia.networking.protocol.MinecraftOutput
import fyi.pauli.ichor.gaia.networking.protocol.desc.ProtocolDesc
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumElementDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractProtocolDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftNumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftStringEncoder.writeString
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarIntSerializer.writeVarInt
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.VarLongSerializer
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.internal.TaggedEncoder

/**
 * @author btwonion
 * @since 11/11/2023
 */

@OptIn(InternalSerializationApi::class)
class MinecraftProtocolEncoder(
	private val output: MinecraftOutput
) : TaggedEncoder<ProtocolDesc>() {

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		return extractProtocolDescriptor(this@getTag, index)
	}

	override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder = runBlocking {
		writeVarInt(collectionSize) { output.writeByte(it) }
		super.beginCollection(descriptor, collectionSize)
	}

	override fun encodeTaggedInt(tag: ProtocolDesc, value: Int) = runBlocking {
		when (tag.type) {
			MinecraftNumberType.DEFAULT -> output.writeInt(value)
			MinecraftNumberType.VAR -> writeVarInt(value) { output.writeByte(it) }
			MinecraftNumberType.UNSIGNED -> output.writeInt(value.toUInt().toInt())
		}
	}

	override fun encodeTaggedByte(tag: ProtocolDesc, value: Byte) = runBlocking {
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> output.writeByte(value.toUByte().toByte())
			else -> output.writeByte(value)
		}
	}

	override fun encodeTaggedShort(tag: ProtocolDesc, value: Short) = runBlocking {
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> output.writeShort(value.toUShort().toShort())
			else -> output.writeShort(value)
		}
	}

	override fun encodeTaggedLong(tag: ProtocolDesc, value: Long) = runBlocking {
		when (tag.type) {
			MinecraftNumberType.VAR -> VarLongSerializer.writeVarLong(value) { output.writeByte(it) }
			else -> output.writeLong(value)
		}
	}

	override fun encodeTaggedFloat(tag: ProtocolDesc, value: Float) = runBlocking {
		output.writeFloat(value)
	}

	override fun encodeTaggedDouble(tag: ProtocolDesc, value: Double) = runBlocking {
		output.writeDouble(value)
	}

	override fun encodeTaggedBoolean(tag: ProtocolDesc, value: Boolean) = runBlocking {
		output.writeBoolean(value)
	}

	override fun encodeTaggedString(tag: ProtocolDesc, value: String) = runBlocking {
		writeString(value, { output.writeByte(it) }) { output.writeFully(it) }
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun encodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor, ordinal: Int) = runBlocking {
		val enumDesc = extractEnumElementDescriptor(enumDescriptor, ordinal)

		when (extractEnumDescriptor(enumDescriptor).type) {
			MinecraftEnumType.VAR_INT -> writeVarInt(enumDesc.ordinal) { output.writeByte(it) }
			MinecraftEnumType.BYTE, MinecraftEnumType.UNSIGNED_BYTE -> output.writeByte(enumDesc.ordinal.toByte())
			MinecraftEnumType.INT -> output.writeInt(enumDesc.ordinal)
			MinecraftEnumType.STRING -> writeString(enumDescriptor.getElementName(ordinal),
				{ output.writeByte(it) }) { output.writeFully(it) }
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
		return when (descriptor.kind) {
			StructureKind.CLASS, StructureKind.LIST -> MinecraftProtocolEncoder(output)
			else -> super.beginStructure(descriptor)
		}
	}
}