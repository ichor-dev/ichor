package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.buffer
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.LONG
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class PingResponse(
  val payload: Long
) : ServerPacket() {

  override suspend fun serialize(): ByteBuffer {
    return buffer(INT + LONG) {
      putInt(id)
      putLong(payload)
    }
  }

  override val id: Int
    get() = 0x01

  override val state: State
    get() = State.STATUS
}