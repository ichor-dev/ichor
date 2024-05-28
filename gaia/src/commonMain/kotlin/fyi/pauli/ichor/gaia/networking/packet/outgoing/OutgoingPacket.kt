package fyi.pauli.ichor.gaia.networking.packet.outgoing

import fyi.pauli.ichor.gaia.networking.packet.State
import kotlinx.serialization.Transient

public abstract class OutgoingPacket {
	public abstract val id: Int

	@Transient
	public abstract val state: State

	@Transient
	public abstract val debugName: String
}