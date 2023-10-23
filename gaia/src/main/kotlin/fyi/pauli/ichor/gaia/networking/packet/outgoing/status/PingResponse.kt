package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.long
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * The response packet for PingRequest.
 *
 * @param payload Should be the same as sent by the client.
 */
data class PingResponse(
	var payload: Long
) : OutgoingPacket() {

	override fun serialize(): RawPacket {
		return packet {
			long(payload)
		}
	}

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Ping Response"
}