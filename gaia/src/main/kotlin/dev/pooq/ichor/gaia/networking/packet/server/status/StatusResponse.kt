package dev.pooq.ichor.gaia.networking.packet.server.status

import dev.pooq.ichor.gaia.extensions.bytes.buffer
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.uncompressedBuffer
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class StatusResponse(
  val jsonResponse: String
) : ServerPacket() {

  override suspend fun serialize(): ByteBuffer {
    return uncompressedBuffer(INT + jsonResponse.length) {
      putInt(id)
      string(jsonResponse)
    }
  }

  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.STATUS
}
