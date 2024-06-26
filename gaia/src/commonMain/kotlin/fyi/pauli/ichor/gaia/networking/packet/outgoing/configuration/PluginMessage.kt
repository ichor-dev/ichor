package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.models.payload.Payload
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses several plugin channels.
 * These internal channels are in the `minecraft` namespace.
 *
 * @param payload The payload to send.
 */
@Serializable
public data class PluginMessage(
	var payload: Payload,
) : OutgoingPacket() {
	override val id: Int
		get() = 0x00
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Plugin Message"
}