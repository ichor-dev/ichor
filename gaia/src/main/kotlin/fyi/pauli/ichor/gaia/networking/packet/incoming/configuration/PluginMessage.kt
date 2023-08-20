package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Mods and plugins can use this to send their data. Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 *
 * @param channel Name of the plugin channel used to send the data.
 * @param data Any data, depending on the channel. The length of this array must be inferred from the packet length.
 */
data class PluginMessage(var channel: Identifier, var data: ByteArray) : IncomingPacket() {
	companion object : PacketProcessor<PluginMessage>() {
		override suspend fun deserialize(buffer: ByteBuffer): PluginMessage {
			return PluginMessage(buffer.identifier(), buffer.rawBytes() ?: error("Empty payload received!"))
		}
	}
}