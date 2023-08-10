package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.compressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

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

	override fun serialize(): ByteBuffer {
		return compressedBuffer {
			string(reason)
		}
	}
}