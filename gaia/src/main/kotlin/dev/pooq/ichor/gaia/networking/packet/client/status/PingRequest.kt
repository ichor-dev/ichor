package dev.pooq.ichor.gaia.networking.packet.client.status

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class PingRequest(
  val payload: Long
) : ClientPacket() {

  override val id: Int
    get() = 0x01

  override val state: State
    get() = State.STATUS

  companion object : PacketDeserializer<PingRequest>() {
    override suspend fun deserialize(byteBuffer: ByteBuffer): PingRequest {
      return PingRequest(
        byteBuffer.long
      )
    }
  }
}