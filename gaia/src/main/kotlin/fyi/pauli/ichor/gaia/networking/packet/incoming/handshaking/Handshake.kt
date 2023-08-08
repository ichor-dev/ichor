package fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking

import fyi.pauli.ichor.gaia.extensions.bytes.short
import fyi.pauli.ichor.gaia.extensions.bytes.string
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

data class Handshake(
	val protocolVersion: Int, val serverAddress: String, val serverPort: Short, val nextState: State
) : IncomingPacket() {

	companion object : PacketDeserializer<Handshake>() {
		override suspend fun deserialize(byteBuffer: ByteBuffer): Handshake {
			return Handshake(
				byteBuffer.varInt(), byteBuffer.string(), byteBuffer.short(), when (byteBuffer.varInt()) {
					1 -> State.STATUS
					2 -> State.LOGIN

					else -> throw IllegalArgumentException("StateId must be 1 or 2.")
				}
			)
		}
	}
}