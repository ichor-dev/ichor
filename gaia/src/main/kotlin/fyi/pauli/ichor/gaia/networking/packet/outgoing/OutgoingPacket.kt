package fyi.pauli.ichor.gaia.networking.packet.outgoing

import fyi.pauli.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

abstract class OutgoingPacket {
	abstract val id: Int
	abstract val state: State
	abstract fun serialize(): ByteBuffer
}