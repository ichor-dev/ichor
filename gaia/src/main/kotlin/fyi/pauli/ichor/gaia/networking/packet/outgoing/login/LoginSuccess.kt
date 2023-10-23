package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.userProfile
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * This packet switches the state to Configuration.
 *
 * @param userProfile The userprofile of the joining player.
 */
data class LoginSuccess(
	var userProfile: UserProfile
) : OutgoingPacket() {
	override fun serialize(): RawPacket {
		return packet {
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