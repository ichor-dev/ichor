package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Acknowledgement to the Login Success packet sent by the server.
 */
class LoginAcknowledged : IncomingPacket() {
	companion object : PacketProcessor<LoginAcknowledged>() {
		override suspend fun deserialize(buffer: ByteBuffer): LoginAcknowledged {
			return LoginAcknowledged()
		}
	}
}