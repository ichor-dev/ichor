package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.bytes.uncompressedBuffer
import dev.pooq.ichor.gaia.extensions.bytes.varLong
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class PingResponse(
  val payload: Long
) : ServerPacket() {

  override fun serialize(): ByteBuffer {
    return uncompressedBuffer {
      varLong(payload)
    }
  }

  override val id: Int
    get() = 0x01

  override val state: State
    get() = State.STATUS
}