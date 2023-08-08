package fyi.pauli.ichor.gaia.networking.packet.incoming.status

import fyi.pauli.ichor.gaia.extensions.bytes.varLong
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

class PingRequest(var payload: Long) : IncomingPacket() {

    companion object : PacketDeserializer<PingRequest>() {
        override suspend fun deserialize(byteBuffer: ByteBuffer): PingRequest {
            return PingRequest(
                byteBuffer.varLong()
            )
        }
    }
}