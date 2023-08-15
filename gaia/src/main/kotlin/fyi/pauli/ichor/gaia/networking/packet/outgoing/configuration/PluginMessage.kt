package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses several plugin channels.
 * These internal channels are in the `minecraft` namespace.
 *
 * @param identifier Name of the plugin channel used to send the data.
 * @param data Any data. The length of this array must be inferred from the packet length.
 */
data class PluginMessage(
	var identifier: Identifier, var data: ByteArray
) : OutgoingPacket() {

	override val id: Int
		get() = 0x00
	override val state: State
		get() = State.CONFIGURATION

	override fun serialize(): ByteBuffer {
		return buffer {
			identifier(identifier)
			rawBytes(data)
		}
	}
}