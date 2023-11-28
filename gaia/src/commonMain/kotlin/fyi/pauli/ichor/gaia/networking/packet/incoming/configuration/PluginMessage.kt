package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Mods and plugins can use this to send their data. Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 *
 * @param channel Name of the plugin channel used to send the data.
 * @param data Any data, depending on the channel. The length of this array must be inferred from the packet length.
 */
@Serializable
public data class PluginMessage(var channel: @Contextual Identifier, var data: ByteArray) : IncomingPacket {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as PluginMessage

		if (channel != other.channel) return false
		if (!data.contentEquals(other.data)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = channel.hashCode()
		result = 31 * result + data.contentHashCode()
		return result
	}
}