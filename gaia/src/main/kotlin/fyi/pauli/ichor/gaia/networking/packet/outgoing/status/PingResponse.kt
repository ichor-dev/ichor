package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varLong
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * The response packet for PingRequest.
 *
 * @param payload Should be the same as sent by the client.
 */
data class PingResponse(
	var payload: Long
) : OutgoingPacket() {

	override fun serialize(): ByteBuffer {
		return uncompressedBuffer {
			varLong(payload)
		}
	}

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.STATUS
}