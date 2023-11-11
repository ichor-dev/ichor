@file:OptIn(ExperimentalSerializationApi::class)

package fyi.pauli.ichor.gaia.networking.protocol.desc

import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * @author btwonion
 * @since 11/11/2023
 */

internal fun extractDescriptor(
	descriptor: SerialDescriptor, index: Int
): ProtocolDesc {
	val format =
		(descriptor.getElementAnnotations(index).firstOrNull() as? MinecraftNumber)?.type ?: MinecraftNumberType.DEFAULT
	val maxStringLength = (descriptor.getElementAnnotations(index).firstOrNull() as? MinecraftString)?.maxLength
		?: MinecraftStringEncoder.MAX_STRING_LENGTH
	return ProtocolDesc(format, maxStringLength)
}

internal fun extractEnumDescriptor(
	descriptor: SerialDescriptor, index: Int
): ProtocolEnumDesc {
	val format =
		(descriptor.getElementAnnotations(index).firstOrNull() as? MinecraftEnum)?.type ?: MinecraftEnumType.VAR_INT
	val stringMaxLength = (descriptor.getElementAnnotations(index).firstOrNull() as? MinecraftString)?.maxLength
		?: MinecraftStringEncoder.MAX_STRING_LENGTH
	return ProtocolEnumDesc(format, stringMaxLength)
}

internal fun extractEnumElementDescriptor(
	descriptor: SerialDescriptor, index: Int
): ProtocolEnumElementDesc {
	val ordinal = (descriptor.getElementAnnotations(index).firstOrNull() as? SerialOrdinal)?.ordinal ?: index

	return ProtocolEnumElementDesc(ordinal)
}