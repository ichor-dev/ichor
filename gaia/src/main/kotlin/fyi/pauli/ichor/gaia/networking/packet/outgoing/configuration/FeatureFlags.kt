package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.list
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

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
	override val debugName: String
		get() = "Feature Flags"

	override fun serialize(): RawPacket {
		return packet {
			list(featureFlags) { identifier(it) }
		}
	}
}