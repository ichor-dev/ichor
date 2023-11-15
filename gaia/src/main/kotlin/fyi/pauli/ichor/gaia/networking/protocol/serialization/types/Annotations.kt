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

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NumberType(
	val type: MinecraftNumberType = MinecraftNumberType.DEFAULT
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class StringLength(
	val maxLength: Int
)

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class EnumType(
	val type: MinecraftEnumType = MinecraftEnumType.VAR_INT
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class EnumSerial(
	val ordinal: Int
)