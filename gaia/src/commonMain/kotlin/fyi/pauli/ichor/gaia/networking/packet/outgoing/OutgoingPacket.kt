package fyi.pauli.ichor.gaia.networking.packet.outgoing

import fyi.pauli.ichor.gaia.networking.packet.State
import kotlinx.serialization.Transient

public interface OutgoingPacket {
	@Transient
	public val id: Int

	@Transient
	public val state: State

	@Transient
	public val debugName: String
}