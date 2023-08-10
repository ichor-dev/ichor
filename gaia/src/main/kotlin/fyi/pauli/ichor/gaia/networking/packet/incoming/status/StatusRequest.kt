package fyi.pauli.ichor.gaia.networking.packet.incoming.status

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * The status can only be requested once immediately after the handshake, before any ping.
 * The server won't respond otherwise.
 */
class StatusRequest : IncomingPacket() {
	companion object : PacketProcessor<StatusRequest>() {
		override suspend fun deserialize(buffer: ByteBuffer): StatusRequest {
			return StatusRequest()
		}
	}
}

