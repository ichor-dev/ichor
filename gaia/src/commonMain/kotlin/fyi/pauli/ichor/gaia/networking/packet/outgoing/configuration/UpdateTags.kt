package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Packet to update tags
 *
 * @param tags The tags
 */
@Serializable
public data class UpdateTags(var tags: MutableMap<@Contextual Identifier, MutableMap<@Contextual Identifier, IntArray>>) :
	OutgoingPacket {
	override val id: Int
		get() = 0x08
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Update Tags"
}