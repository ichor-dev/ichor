package fyi.pauli.ichor.gaia.models

import fyi.pauli.ichor.gaia.networking.serialization.IdentifierStringSerializer
import kotlinx.serialization.Serializable

@Serializable(with = IdentifierStringSerializer::class)
public data class Identifier(val namespace: String, val value: String) {
	override fun toString(): String {
		return "$namespace:$value"
	}
}