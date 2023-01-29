package dev.pooq.ichor.gaia.networking.packet.client.handshaking

import dev.pooq.ichor.gaia.extensions.short
import dev.pooq.ichor.gaia.extensions.string
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.networking.packet.handler.handshaking.HandshakeHandler
import java.nio.ByteBuffer

data class Handshake(
  val protocolVersion: Int,
  val serverAddress: String,
  val serverPort: Short,
  val nextState: State
) : ClientPacket() {

  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.HANDSHAKING

  companion object : PacketDeserializer<Handshake>(HandshakeHandler()) {
    override suspend fun deserialize(byteBuffer: ByteBuffer): Handshake {
      return Handshake(
        byteBuffer.varInt(),
        byteBuffer.string(),
        byteBuffer.short(),
        NextState.of(byteBuffer.varInt())
      )
    }
  }

  object NextState {
    fun of(stateId: Int): State {
      return when (stateId) {
        1 -> State.STATUS
        2 -> State.LOGIN

        else -> throw IllegalArgumentException("StateId must be 1 or 2.")
      }
    }
  }
}