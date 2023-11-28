package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

/**
 * The response packet for StatusRequest.
 *
 * @param status The status response
 */
@Serializable
public data class StatusResponse(
	var status: String
) : OutgoingPacket() {
	override val id: Int
		get() = 0x00

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Status Response"
}
