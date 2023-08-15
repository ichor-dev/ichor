package fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking

import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.unsignedShort
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.varInt
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

data class Handshake(
	val protocolVersion: Int, val serverAddress: String, val serverPort: Short, val nextState: State
) : IncomingPacket() {
	companion object : PacketProcessor<Handshake>() {
		override suspend fun deserialize(buffer: ByteBuffer): Handshake {
			return Handshake(
				buffer.varInt(), buffer.string(), buffer.unsignedShort(), when (buffer.varInt()) {
					1 -> State.STATUS
					2 -> State.LOGIN

					else -> throw IllegalArgumentException("StateId must be 1 or 2.")
				}
			)
		}
	}
}