package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * Packet to disconnect a player during the configuration state
 *
 * @param reason The reason, which is displayed to the player's screen
 */
data class Disconnect(var reason: String) : OutgoingPacket() {
	override val id: Int
		get() = 0x01
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Disconnect"

	override fun serialize(): RawPacket {
		return packet {
			string(reason)
		}
	}
}