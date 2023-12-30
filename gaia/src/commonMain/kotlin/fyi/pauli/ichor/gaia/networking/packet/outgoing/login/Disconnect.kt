package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

/**
 * Packet, which disconnects the player during the login state.
 *
 * @param reason The reason why the player was disconnected.
 */
@Serializable
public data class Disconnect(var reason: String) : OutgoingPacket {
	override val id: Int
		get() = 0x00
	override val state: State
		get() = State.LOGIN
	override val debugName: String
		get() = "Disconnect"
}