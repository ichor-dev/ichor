package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.compressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
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

	override fun serialize(): ByteBuffer {
		return compressedBuffer {
			varInt(pingId)
		}
	}
}