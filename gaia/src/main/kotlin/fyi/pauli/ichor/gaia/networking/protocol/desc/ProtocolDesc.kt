package fyi.pauli.ichor.gaia.networking.protocol.desc

import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftEnumType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.MinecraftNumberType

/**
 * @author btwonion
 * @since 11/11/2023
 */

data class ProtocolDesc(
	val type: MinecraftNumberType,
	val maxStringLength: Int
)

data class ProtocolEnumDesc(
	val type: MinecraftEnumType,
	val stringMaxLength: Int
)

data class ProtocolEnumElementDesc(
	val ordinal: Int
)