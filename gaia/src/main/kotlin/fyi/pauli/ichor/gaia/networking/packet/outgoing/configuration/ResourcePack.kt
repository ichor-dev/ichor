package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.boolean
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * Packet used to prompt players a resource pack.
 *
 * @param url The URL to the resource pack.
 * @param hash A 40-character hexadecimal and lowercase SHA-1 hash of the resource pack file.
 * If it's not a 40-character hexadecimal string, the client will not use it for hash verification and likely waste bandwidth — but it will still treat it as a unique id
 * @param forced The Notchian client will be forced to use the resource pack from the server. If they decline they will be kicked from the server.
 * @param hasPromptMessage true If the next field will be sent false otherwise. When false, this is the end of the packet
 * @param promptMessage This is shown in the prompt making the client accept or decline the resource pack.
 */
data class ResourcePack(
	var url: String, var hash: String, var forced: Boolean, var hasPromptMessage: Boolean, var promptMessage: String?
) : OutgoingPacket() {
	override val id: Int
		get() = 0x06
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Resource Pack"

	override fun serialize(): RawPacket {
		return packet {
			string(url)
			string(hash)
			boolean(forced)
			boolean(hasPromptMessage)
			if (hasPromptMessage) string(promptMessage ?: error("Prompt message enabled, but no message provided!"))
		}
	}
}