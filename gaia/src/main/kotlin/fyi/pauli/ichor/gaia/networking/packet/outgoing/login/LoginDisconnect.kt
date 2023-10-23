package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * Packet, which disconnects the player during the login state.
 *
 * @param reason The reason why the player was disconnected.
 */
data class Disconnect(var reason: String) : OutgoingPacket() {
	override val id: Int
		get() = 0x00
	override val state: State
		get() = State.LOGIN
	override val debugName: String
		get() = "Disconnect"

	override fun serialize(): RawPacket {
		return packet {
			string(reason)
		}
	}
}