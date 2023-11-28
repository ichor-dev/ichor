package fyi.pauli.ichor.gaia.models.payload

import fyi.pauli.ichor.gaia.models.Identifier
import kotlinx.serialization.Serializable

@Serializable
public data class BrandPayload(val brand: String) : Payload {
	override val identifier: Identifier
		get() = Identifier("minecraft", "brand")
}