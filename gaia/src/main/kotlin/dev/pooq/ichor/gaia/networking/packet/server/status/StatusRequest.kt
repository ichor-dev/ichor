package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.buffer
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class StatusRequest(
  override val id: Int,
  override val state: State
) : ServerPacket() {

  override fun serialize(): ByteBuffer {
    return buffer(INT){
      putInt(id)
    }
  }
}