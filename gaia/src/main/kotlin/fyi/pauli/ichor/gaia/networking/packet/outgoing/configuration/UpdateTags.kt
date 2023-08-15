package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Packet to update tags
 *
 * @param tags The tags
 */
data class UpdateTags(var tags: MutableMap<Identifier, MutableMap<Identifier, IntArray>>) : OutgoingPacket() {
	override val id: Int
		get() = 0x08
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Update Tags"
	override fun serialize(): ByteBuffer {
		return buffer {
			varInt(tags.size)
			tags.forEach { (key, tag) ->
				identifier(key)
				varInt(tag.size)
				tag.forEach { (tagIdentifier, id) ->
					identifier(tagIdentifier)
					varInt(id.size)
					id.forEach { varInt(it) }
				}
			}
		}
	}
}