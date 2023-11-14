package fyi.pauli.ichor.gaia.networking.protocol.serialization

import fyi.pauli.ichor.gaia.networking.protocol.MinecraftOutput
import fyi.pauli.ichor.gaia.networking.protocol.desc.*
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumElementDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumParameters
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

	@ExperimentalSerializationApi
	override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean {
		return true
	}

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		return extractDescriptor(this@getTag, index)
	}

	override fun encodeTaggedInt(tag: ProtocolDesc, value: Int) = runBlocking {
		when (tag.type) {
			MinecraftNumberType.DEFAULT, MinecraftNumberType.UNSIGNED -> output.writeInt(value)
			MinecraftNumberType.VAR -> writeVarInt(value) { output.writeByte(it) }
		}
	}

	override fun encodeTaggedByte(tag: ProtocolDesc, value: Byte) = runBlocking {
		output.writeByte(value)
	}

	override fun encodeTaggedShort(tag: ProtocolDesc, value: Short) = runBlocking {
		output.writeShort(value)
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

		when (extractEnumParameters(enumDescriptor).type) {
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
			is StructureKind.CLASS -> {
				return MinecraftProtocolEncoder(output)
			}

			is StructureKind.LIST -> {
				super.beginStructure(descriptor)
			}

			else -> super.beginStructure(descriptor)
		}
	}
}