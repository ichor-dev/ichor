package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Sent by the client to notify the client that the configuration process has finished.
 * It is sent in response to the server's Finish Configuration.
 */
class FinishConfiguration : IncomingPacket() {
	companion object : PacketProcessor<FinishConfiguration>() {
		override suspend fun deserialize(buffer: ByteBuffer): FinishConfiguration {
			return FinishConfiguration()
		}
	}
}