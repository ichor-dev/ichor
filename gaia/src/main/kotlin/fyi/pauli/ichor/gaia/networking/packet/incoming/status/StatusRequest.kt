package fyi.pauli.ichor.gaia.networking.packet.incoming.status

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

class StatusRequest : IncomingPacket() {

    companion object : PacketProcessor<StatusRequest>() {
        override suspend fun deserialize(byteBuffer: ByteBuffer): StatusRequest {
            return StatusRequest()
        }
    }
}

