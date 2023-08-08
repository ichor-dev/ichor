package fyi.pauli.ichor.gaia.networking.packet.server.status

import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.networking.ServerPacket
import fyi.pauli.ichor.gaia.networking.packet.State
import io.github.oshai.kotlinlogging.KLogger
import org.koin.java.KoinJavaComponent.inject
import java.nio.ByteBuffer

class StatusResponse(
	val jsonResponse: String
) : ServerPacket() {

	override fun serialize(): ByteBuffer {

		val logger: KLogger by inject(KLogger::class.java)

		logger.debug { jsonResponse }

		return uncompressedBuffer {
			string(jsonResponse)
		}
	}

	override val id: Int
		get() = 0x00

	override val state: State
		get() = State.STATUS
}
