package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.buffer
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.compoundTag
import fyi.pauli.ichor.gaia.models.nbt.impl.CompoundTag
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * Represents certain registries that are sent from the server and are applied on the client.
 *
 * @param registryCodec CompoundTag, which contains the registries mentioned here:
 * @see "https://wiki.vg/Pre-release_protocol#Registry_Data"
 */
data class RegistryData(var registryCodec: CompoundTag) : OutgoingPacket() {
	override val id: Int
		get() = 0x05
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Registry Data"
	override fun serialize(): RawPacket {
		return buffer {
			compoundTag(registryCodec)
		}
	}
}