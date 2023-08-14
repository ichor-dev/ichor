package fyi.pauli.ichor.gaia.models.payload

import fyi.pauli.ichor.gaia.models.Identifier
import java.nio.ByteBuffer

interface Payload {
	val identifier: Identifier
	fun write(buffer: ByteBuffer)
}