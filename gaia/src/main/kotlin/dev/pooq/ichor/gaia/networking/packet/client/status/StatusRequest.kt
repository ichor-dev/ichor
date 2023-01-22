package dev.pooq.ichor.gaia.networking.packet.client.status

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class StatusRequest : ClientPacket() {

  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.STATUS

  companion object : PacketDeserializer<StatusRequest> {
    override fun deserialize(byteBuffer: ByteBuffer): StatusRequest {
      return StatusRequest()
    }
  }
}

