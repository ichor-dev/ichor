package fyi.pauli.ichor.gaia.models.payload

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.models.Identifier
import java.nio.ByteBuffer

data class BrandPayload(val brand: String): Payload {
	override val identifier: Identifier
		get() = Identifier("minecraft", "brand")

	override fun write(buffer: ByteBuffer) {
		buffer.string(brand)
	}
}