package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.boolean
import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.uuid
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer
import java.util.*

class LoginStart(
	var name: String, var hasPlayerUUID: Boolean, var uuid: UUID? = null
) : IncomingPacket() {

	companion object : PacketDeserializer<LoginStart>() {
		override suspend fun deserialize(byteBuffer: ByteBuffer): LoginStart {
			return LoginStart(byteBuffer.string(), byteBuffer.boolean()).apply {
				if (hasPlayerUUID) uuid = byteBuffer.uuid()
			}
		}
	}
}