package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.uncompressedBuffer
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

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

	override fun serialize(): ByteBuffer {
		return uncompressedBuffer {
			string(reason)
		}
	}
}