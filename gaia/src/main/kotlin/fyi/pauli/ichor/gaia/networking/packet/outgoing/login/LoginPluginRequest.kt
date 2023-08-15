package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.rawBytes
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Used to implement a custom handshaking flow together with Login Plugin Response.
 *
 * Unlike plugin messages in "play" mode, these messages follow a lock-step request/response scheme, where the client is expected to respond to a request indicating whether it understood.
 * The notchian client always responds that it hasn't understood, and sends an empty payload.
 *
 * @param messageId Generated by the server - should be unique to the connection.
 * @param channel Name of the plugin channel used to send the data.
 * @param data Any data, depending on the channel. The length of this array must be inferred from the packet length.
 */
data class LoginPluginRequest(var messageId: Int, var channel: Identifier, var data: ByteArray) : OutgoingPacket() {
	override val id: Int
		get() = 0x04
	override val state: State
		get() = State.LOGIN

	override fun serialize(): ByteBuffer {
		return buffer {
			varInt(messageId)
			identifier(channel)
			rawBytes(data)
		}
	}
}