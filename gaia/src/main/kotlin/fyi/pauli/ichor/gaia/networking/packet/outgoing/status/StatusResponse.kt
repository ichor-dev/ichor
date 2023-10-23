package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * The response packet for StatusRequest.
 *
 * @param status The status response
 */
data class StatusResponse(
	var status: String
) : OutgoingPacket() {

	override fun serialize(): RawPacket {
		return packet {
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
