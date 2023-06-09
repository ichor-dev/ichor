package dev.pooq.ichor.gaia.networking.packet.client.login

import dev.pooq.ichor.gaia.extensions.bytes.boolean
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.uuid
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.networking.packet.receive.receivers.login.LoginReceivers
import java.nio.ByteBuffer
import java.util.*

class LoginStart(
	val name: String,
	val hasPlayerUUID: Boolean,
	var uuid: UUID? = null
) : ClientPacket() {

	override val id: Int
		get() = 0x00

	override val state: State
		get() = State.LOGIN

	companion object : PacketProcessor<LoginStart>(LoginReceivers.LoginStartReceiver) {
		override suspend fun deserialize(byteBuffer: ByteBuffer): LoginStart {
			return LoginStart(byteBuffer.string(), byteBuffer.boolean()).apply {
				if (hasPlayerUUID) uuid = byteBuffer.uuid()
			}
		}
	}

}