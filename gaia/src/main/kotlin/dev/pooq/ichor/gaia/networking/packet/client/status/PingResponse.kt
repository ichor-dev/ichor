package dev.pooq.ichor.gaia.networking.packet.client.status

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class PingResponse(
  override val id: Int,
  override val state: State,
  val payload: Long
) : ClientPacket() {

  companion object : PacketDeserializer<PingResponse>{
    override fun deserialize(id: Int, byteBuffer: ByteBuffer): PingResponse {
      return PingResponse(
        id,
        State.STATUS,
        byteBuffer.long
      )
    }
  }
}