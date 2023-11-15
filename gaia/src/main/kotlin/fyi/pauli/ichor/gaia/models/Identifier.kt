package fyi.pauli.ichor.gaia.models

import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.objects.IdentifierByteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = IdentifierByteSerializer::class)
data class Identifier(val namespace: String, val value: String) {
	override fun toString(): String {
		return "$namespace:$value"
	}
}