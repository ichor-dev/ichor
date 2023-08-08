package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

data class PacketIdentifier(val id: Int, val state: State, val debuggingName: String = "unnamed packet")

abstract class IncomingPacket {

	abstract class PacketDeserializer<P : IncomingPacket> {
		internal abstract suspend fun deserialize(byteBuffer: ByteBuffer): P
	}
}