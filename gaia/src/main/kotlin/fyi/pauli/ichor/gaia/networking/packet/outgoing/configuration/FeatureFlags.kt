package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.compressedBuffer
import fyi.pauli.ichor.gaia.extensions.bytes.identifier
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Used to enable and disable features, generally experimental ones, on the client.
 *
 * @param totalFeatures Number of features that appear in the array below.
 * @param featureFlags The identifiers of the features.
 */
data class FeatureFlags(var totalFeatures: Int, var featureFlags: MutableList<Identifier>) : OutgoingPacket() {
	override val id: Int
		get() = 0x07
	override val state: State
		get() = State.CONFIGURATION

	override fun serialize(): ByteBuffer {
		return compressedBuffer {
			varInt(totalFeatures)
			featureFlags.forEach { identifier(it) }
		}
	}
}