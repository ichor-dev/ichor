@file:OptIn(ExperimentalSerializationApi::class)

package fyi.pauli.ichor.gaia.networking.protocol.desc

import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.*
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftNumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftStringEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * @author btwonion
 * @since 11/11/2023
 */

internal fun extractDescriptor(
	descriptor: SerialDescriptor, index: Int
): ProtocolDesc {
	val format = descriptor.findElementAnnotation<MinecraftNumber>(index)?.type ?: MinecraftNumberType.DEFAULT
	val maxStringLength =
		descriptor.findElementAnnotation<MinecraftString>(index)?.maxLength ?: MinecraftStringEncoder.MAX_STRING_LENGTH
	return ProtocolDesc(format, maxStringLength)
}


internal fun extractEnumParameters(
	descriptor: SerialDescriptor
): ProtocolEnumDesc {
	val format = descriptor.findEntityAnnotation<MinecraftEnum>()?.type ?: MinecraftEnumType.VAR_INT
	val stringMaxLength =
		descriptor.findEntityAnnotation<MinecraftString>()?.maxLength ?: MinecraftStringEncoder.MAX_STRING_LENGTH
	return ProtocolEnumDesc(format, stringMaxLength)
}

internal fun extractEnumElementDescriptor(
	descriptor: SerialDescriptor, index: Int
): ProtocolEnumElementDesc {
	val ordinal = descriptor.findElementAnnotation<SerialOrdinal>(index)?.ordinal ?: index

	return ProtocolEnumElementDesc(ordinal)
}

private inline fun <reified A : Annotation> SerialDescriptor.findEntityAnnotation(): A? {
	return annotations.find { it is A } as A?
}

private inline fun <reified A : Annotation> SerialDescriptor.findElementAnnotation(
	elementIndex: Int
): A? {
	return getElementAnnotations(elementIndex).find { it is A } as A?
}

internal fun findEnumIndexByTag(
	descriptor: SerialDescriptor, serialOrdinal: Int
): Int = (0 until descriptor.elementsCount).firstOrNull {
	extractEnumElementDescriptor(
		descriptor, it
	).ordinal == serialOrdinal
} ?: -1