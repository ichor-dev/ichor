package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Used to enable and disable features, generally experimental ones, on the client.
 *
 * @param featureFlags The identifiers of the features.
 */
@Serializable
public data class FeatureFlags(var featureFlags: MutableList<@Contextual Identifier>) : OutgoingPacket() {
	override val id: Int
		get() = 0x07
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Feature Flags"
}