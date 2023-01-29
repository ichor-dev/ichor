package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.buffer
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.LONG
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class PingResponse(
  override val id: Int,
  override val state: State,
  val payload: Long
) : ServerPacket() {

  override suspend fun serialize(): ByteBuffer {
    return buffer(INT + LONG){
      putInt(id)
      putLong(payload)
    }
  }
}