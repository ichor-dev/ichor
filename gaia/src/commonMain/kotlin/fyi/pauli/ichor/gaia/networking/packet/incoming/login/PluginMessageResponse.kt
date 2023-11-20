package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

/**
 * Response packet for LoginPluginRequest
 *
 * @param messageId Should match ID from server.
 * @param successful true if the client understood the request, false otherwise. When false, no payload follows.
 * @param data Optional. Any data, depending on the channel. The length of this array must be inferred from the packet length.
 */
@Serializable
public data class PluginMessageResponse(var messageId: Int, var successful: Boolean, var data: ByteArray?) :
	IncomingPacket {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as PluginMessageResponse

		if (messageId != other.messageId) return false
		if (successful != other.successful) return false
		if (data != null) {
			if (other.data == null) return false
			if (!data.contentEquals(other.data)) return false
		} else if (other.data != null) return false

		return true
	}

	override fun hashCode(): Int {
		var result = messageId
		result = 31 * result + successful.hashCode()
		result = 31 * result + (data?.contentHashCode() ?: 0)
		return result
	}
}