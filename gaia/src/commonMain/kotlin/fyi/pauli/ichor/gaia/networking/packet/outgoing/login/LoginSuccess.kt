package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * This packet switches the state to Configuration.
 *
 * @param userProfile The userprofile of the joining player.
 */
@Serializable
public data class LoginSuccess(
	var userProfile: @Contextual UserProfile
) : OutgoingPacket {
	override val id: Int
		get() = 0x02
	override val state: State
		get() = State.LOGIN
	override val debugName: String
		get() = "Login Success"
}