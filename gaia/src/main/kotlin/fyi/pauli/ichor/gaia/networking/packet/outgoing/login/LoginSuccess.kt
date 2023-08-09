package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.extensions.bytes.*
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer
import java.util.*

data class LoginSuccess(
	var uuid: UUID, var username: String, var propertiesCount: Int, var properties: List<Property>
) : OutgoingPacket() {
	override fun serialize(): ByteBuffer {
		return compressedBuffer {
			varLong(uuid.mostSignificantBits)
			varLong(uuid.leastSignificantBits)

			string(username)
			varInt(propertiesCount)

			properties.forEach {
				string(it.name)
				string(it.value)
				boolean(true)
				string(it.signature)
			}
		}
	}

	override val id: Int
		get() = 0x02
	override val state: State
		get() = State.LOGIN
}