package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.userProfile
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * This packet switches the state to Configuration.
 *
 * @param userProfile The userprofile of the joining player.
 */
data class LoginSuccess(
	var userProfile: UserProfile
) : OutgoingPacket() {
	override fun serialize(): ByteBuffer {
		return buffer {
			userProfile(userProfile)
		}
	}

	override val id: Int
		get() = 0x02
	override val state: State
		get() = State.LOGIN
	override val debugName: String
		get() = "Login Success"
}