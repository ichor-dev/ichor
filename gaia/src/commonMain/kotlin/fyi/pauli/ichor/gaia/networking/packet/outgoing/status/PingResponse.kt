package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.prolialize.serialization.types.NumberType
import kotlinx.serialization.Serializable

/**
 * The response packet for PingRequest.
 *
 * @param payload Should be the same as sent by the client.
 */
@Serializable
public data class PingResponse(
	@NumberType var payload: Long
) : OutgoingPacket {

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Ping Response"
}