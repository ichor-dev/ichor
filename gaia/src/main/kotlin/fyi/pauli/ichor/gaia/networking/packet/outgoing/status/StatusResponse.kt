package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import io.github.oshai.kotlinlogging.KLogger
import org.koin.java.KoinJavaComponent.inject
import java.nio.ByteBuffer

/**
 * The response packet for StatusRequest.
 *
 * @param status The status response
 */
data class StatusResponse(
	var status: String
) : OutgoingPacket() {

	override fun serialize(): ByteBuffer {
		return buffer {
			string(status)
		}
	}

	override val id: Int
		get() = 0x00

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Status Response"
}
