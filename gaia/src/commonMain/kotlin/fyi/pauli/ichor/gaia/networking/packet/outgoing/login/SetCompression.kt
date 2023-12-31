package fyi.pauli.ichor.gaia.networking.packet.outgoing.login

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

/**
 * Enables compression.
 * If compression is enabled, all following packets are encoded in the compressed packet format.
 * Negative values will disable compression, meaning the packet format should remain in the uncompressed packet format.
 * However, this packet is entirely optional, and if not sent, compression will also not be enabled
 * (the notchian server does not send the packet when compression is disabled).
 *
 * @param threshold Maximum size of a packet before it is compressed.
 */
@Serializable
public data class SetCompression(var threshold: Int) : OutgoingPacket() {
	override val id: Int
		get() = 0x03
	override val state: State
		get() = State.LOGIN
	override val debugName: String
		get() = "Set Compression"
}