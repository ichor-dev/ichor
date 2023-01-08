package dev.pooq.ichor.gaia.networking.packet.client.status

import dev.pooq.ichor.gaia.extensions.string
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

data class StatusResponse(
  override val id: Int,
  override val state: State,
  val jsonResponse: String
) : ClientPacket() {

  companion object : PacketDeserializer<StatusResponse>{

    override fun deserialize(id: Int, byteBuffer: ByteBuffer): StatusResponse {
      return StatusResponse(
        id,
        State.STATUS,
        byteBuffer.string()
      )
    }
  }
}