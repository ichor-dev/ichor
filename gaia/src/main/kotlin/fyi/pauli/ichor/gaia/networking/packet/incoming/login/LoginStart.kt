package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.uuid
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer
import java.util.*

/**
 * Packet, used to start the login sequence.
 *
 * @param name Player's Username.
 * @param uuid The UUID of the player logging in.
 */
data class LoginStart(
	var name: String, var uuid: UUID
) : IncomingPacket() {
	companion object : PacketProcessor<LoginStart>() {
		override suspend fun deserialize(buffer: ByteBuffer): LoginStart {
			return LoginStart(buffer.string(), buffer.uuid())
		}
	}
}