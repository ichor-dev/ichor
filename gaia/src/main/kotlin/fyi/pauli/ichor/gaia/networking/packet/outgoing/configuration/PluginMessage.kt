package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.models.payload.Payload
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses several plugin channels.
 * These internal channels are in the `minecraft` namespace.
 *
 * @param payload The payload to send.
 */
data class PluginMessage(
	var payload: Payload
) : OutgoingPacket() {

	override val id: Int
		get() = 0x00
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Plugin Message"

	override fun serialize(): RawPacket {
		return buffer {
			identifier(payload.identifier)
			payload.write(this)
		}
	}
}