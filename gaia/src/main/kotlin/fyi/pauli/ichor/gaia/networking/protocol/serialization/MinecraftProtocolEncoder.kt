package fyi.pauli.ichor.gaia.networking.protocol.serialization

import fyi.pauli.ichor.gaia.networking.protocol.MinecraftOutput
import fyi.pauli.ichor.gaia.networking.protocol.desc.ProtocolDesc
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.desc.extractEnumElementDescriptor
import fyi.pauli.ichor.gaia.networking.protocol.protocolScope
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftNumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftStringEncoder.writeString
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.VarIntEncoder.writeVarInt
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
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

	override fun encodeTaggedInt(tag: ProtocolDesc, value: Int) {
		protocolScope.launch {
			when (tag.type) {
				MinecraftNumberType.DEFAULT, MinecraftNumberType.UNSIGNED -> output.writeInt(value)
				MinecraftNumberType.VAR -> writeVarInt(value) { output.writeByte(it) }
			}
		}
	}

	override fun encodeTaggedByte(tag: ProtocolDesc, value: Byte) {
		protocolScope.launch {
			output.writeByte(value)
		}
	}

	override fun encodeTaggedShort(tag: ProtocolDesc, value: Short) {
		protocolScope.launch {
			output.writeShort(value)
		}
	}

	override fun encodeTaggedLong(tag: ProtocolDesc, value: Long) {
		protocolScope.launch {
			output.writeLong(value)
		}
	}

	override fun encodeTaggedFloat(tag: ProtocolDesc, value: Float) {
		protocolScope.launch {
			output.writeFloat(value)
		}
	}

	override fun encodeTaggedDouble(tag: ProtocolDesc, value: Double) {
		protocolScope.launch {
			output.writeDouble(value)
		}
	}

	override fun encodeTaggedBoolean(tag: ProtocolDesc, value: Boolean) {
		protocolScope.launch {
			output.writeBoolean(value)
		}
	}

	override fun encodeTaggedString(tag: ProtocolDesc, value: String) {
		protocolScope.launch {
			writeString(value, { output.writeByte(it) }) { output.writeFully(it) }
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun encodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor, ordinal: Int) {
		protocolScope.launch {
			val enumDesc = extractEnumElementDescriptor(enumDescriptor, ordinal)

			when (extractEnumDescriptor(enumDescriptor, ordinal).type) {
				MinecraftEnumType.VAR_INT -> writeVarInt(enumDesc.ordinal) { output.writeByte(it) }
				MinecraftEnumType.BYTE, MinecraftEnumType.UNSIGNED_BYTE -> output.writeByte(enumDesc.ordinal.toByte())
				MinecraftEnumType.INT -> output.writeInt(enumDesc.ordinal)
				MinecraftEnumType.STRING -> writeString(enumDescriptor.getElementName(ordinal),
					{ output.writeByte(it) }) { output.writeFully(it) }
			}
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