package fyi.pauli.ichor.gaia.networking.packet.outgoing

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.networking.packet.State

abstract class OutgoingPacket {
	abstract val id: Int
	abstract val state: State
	abstract val debugName: String
	abstract fun serialize(): RawPacket
}