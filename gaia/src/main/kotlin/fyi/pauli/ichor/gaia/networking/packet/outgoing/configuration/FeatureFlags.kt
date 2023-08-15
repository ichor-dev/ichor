package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.list
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Used to enable and disable features, generally experimental ones, on the client.
 *
 * @param featureFlags The identifiers of the features.
 */
data class FeatureFlags(var featureFlags: MutableList<Identifier>) : OutgoingPacket() {
	override val id: Int
		get() = 0x07
	override val state: State
		get() = State.CONFIGURATION

	override fun serialize(): ByteBuffer {
		return buffer {
			list(featureFlags) { identifier(it) }
		}
	}
}