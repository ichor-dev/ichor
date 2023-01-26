package dev.pooq.ichor.gaia.networking.packet.client.handshaking

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class LegacyServerListPing : ClientPacket() {

  override val id: Int
    get() = 0xFE

  override val state: State
    get() = State.HANDSHAKING

  companion object : PacketDeserializer<LegacyServerListPing> {
    override fun deserialize(byteBuffer: ByteBuffer): LegacyServerListPing {
      return LegacyServerListPing()
    }
  }
}