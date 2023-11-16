@file:OptIn(ExperimentalSerializationApi::class)

package fyi.pauli.ichor.gaia.networking.protocol.serialization.types

import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

/**
 * @author btwonion
 * @since 11/11/2023
 */
/**
 * Annotation to specify the binary encoding of the number.
 */
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NumberType(
	val type: MinecraftNumberType = MinecraftNumberType.DEFAULT
)

/**
 * Annotation to specify the max string length of a specific string.
 */
@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class StringLength(
	val maxLength: Int
)

/**
 * Annotation to specify the encoding of an enum.
 */
@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class EnumType(
	val type: MinecraftEnumType = MinecraftEnumType.VAR_INT
)

/**
 * Annotation to specify serial number of the targetted enum entry.
 */
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class EnumSerial(
	val ordinal: Int
)