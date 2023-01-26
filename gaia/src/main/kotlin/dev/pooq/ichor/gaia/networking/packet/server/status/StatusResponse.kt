package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.buffer
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class StatusResponse(
  val jsonResponse: String
) : ServerPacket() {

  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.STATUS

  override fun serialize(): ByteBuffer {
    return buffer(INT){
      putInt(id)
    }
  }
}
