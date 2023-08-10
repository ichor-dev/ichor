package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.varLong
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * The server will frequently send out a keep-alive, each containing a random ID.
 * The client must respond with the same packet.
 *
 * @param id ID to check the client response
 */
data class KeepAlive(var id: Long) : IncomingPacket() {
	companion object : PacketProcessor<KeepAlive>() {
		override suspend fun deserialize(buffer: ByteBuffer): KeepAlive {
			return KeepAlive(buffer.varLong())
		}
	}
}