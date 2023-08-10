package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Response to the outgoing packet (Ping) with the same id.
 *
 * @param id id is the same as the ping packet
 */
data class Pong(var id: Int) : IncomingPacket() {
	companion object : PacketProcessor<Pong>() {
		override suspend fun deserialize(buffer: ByteBuffer): Pong {
			return Pong(buffer.varInt())
		}
	}
}