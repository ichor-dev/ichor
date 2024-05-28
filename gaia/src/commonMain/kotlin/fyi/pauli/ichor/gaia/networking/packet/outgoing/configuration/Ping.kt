package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.prolialize.serialization.types.NumberType
import kotlinx.serialization.Serializable

/**
 * When sent to the client, the client responds with a Pong packet with the same id.
 *
 * @param pingId ID to check the response of the client
 */
@Serializable
public data class Ping(
	@NumberType var pingId: Int,
) : OutgoingPacket() {
	override val id: Int
		get() = 0x04
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Ping"
}