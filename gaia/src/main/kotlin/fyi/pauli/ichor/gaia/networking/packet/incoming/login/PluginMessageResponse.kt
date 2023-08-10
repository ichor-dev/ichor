package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.extensions.bytes.boolean
import fyi.pauli.ichor.gaia.extensions.bytes.byteArray
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * Response packet for LoginPluginRequest
 *
 * @param messageId Should match ID from server.
 * @param successful true if the client understood the request, false otherwise. When false, no payload follows.
 * @param data Optional. Any data, depending on the channel. The length of this array must be inferred from the packet length.
 */
data class PluginMessageResponse(var messageId: Int, var successful: Boolean, var data: ByteArray?) : IncomingPacket() {
	companion object : PacketProcessor<PluginMessageResponse>() {
		override suspend fun deserialize(buffer: ByteBuffer): PluginMessageResponse {
			val id = buffer.varInt()
			val successful = buffer.boolean()
			val data = if (!buffer.hasRemaining()) null else buffer.byteArray(buffer.remaining())

			return PluginMessageResponse(id, successful, data)
		}
	}
}