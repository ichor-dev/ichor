package fyi.pauli.ichor.gaia.networking.packet.incoming.status

import fyi.pauli.ichor.gaia.extensions.bytes.varLong
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Requests the ping.
 *
 * @param payload May be any number. Notchian clients use a system-dependent time value which is counted in milliseconds.
 */
data class PingRequest(var payload: Long) : IncomingPacket() {
	companion object : PacketProcessor<PingRequest>() {
		override suspend fun deserialize(buffer: ByteBuffer): PingRequest {
			return PingRequest(
				buffer.varLong()
			)
		}
	}
}