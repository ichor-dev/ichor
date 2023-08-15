package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.int
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * When sent to the client, the client responds with a Pong packet with the same id.
 *
 * @param pingId ID to check the response of the client
 */
data class Ping(var pingId: Int) : OutgoingPacket() {
	override val id: Int
		get() = 0x04
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Ping"
	override fun serialize(): ByteBuffer {
		return buffer {
			int(pingId)
		}
	}
}