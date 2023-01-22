package dev.pooq.ichor.gaia.networking.packet.client.handshaking

import dev.pooq.ichor.gaia.extensions.short
import dev.pooq.ichor.gaia.extensions.string
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

data class Handshake(
  val protocolVersion: Int,
  val serverAddress: String,
  val serverPort: Short,
  val nextState: NextState
) : ClientPacket() {


  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.HANDSHAKING

  companion object : PacketDeserializer<Handshake>{
    override fun deserialize(byteBuffer: ByteBuffer): Handshake {
      return Handshake(
        byteBuffer.varInt(),
        byteBuffer.string(),
        byteBuffer.short(),
        NextState.of(byteBuffer.varInt())
      )
    }

  }

  enum class NextState(
    val stateId: Int
  ){
    STATUS(1),
    LOGIN(2);

    companion object{
      fun of(stateId: Int): NextState{
        return when(stateId){
          1 -> STATUS
          2 -> LOGIN

          else -> throw IllegalArgumentException("StateId must be 1 or 2.")
        }
      }
    }
  }
}