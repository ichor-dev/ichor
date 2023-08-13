package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.varLong
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * The server will frequently send out a keep-alive, each containing a random ID.
 * The client must respond with the same payload.
 * If the client does not respond to them for over 30 seconds, the server kicks the client.
 * Vice versa, if the server does not send any keep-alives for 20 seconds, the client will disconnect and yields a "Timed out" exception.
 *
 * @param keepAliveId ID to check the response of the client
 */
data class KeepAlive(var keepAliveId: Long) : OutgoingPacket() {
	override val id: Int
		get() = 0x03
	override val state: State
		get() = State.CONFIGURATION

	override fun serialize(): ByteBuffer {
		return buffer {
			varLong(keepAliveId)
		}
	}
}