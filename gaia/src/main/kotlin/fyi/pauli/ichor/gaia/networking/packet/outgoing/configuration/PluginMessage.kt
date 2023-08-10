package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.compressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.models.Identifier
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
		return compressedBuffer {
			identifier(identifier)
			put(data)
		}
	}
}