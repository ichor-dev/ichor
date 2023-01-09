package dev.pooq.ichor.gaia.networking.packet.server.handshaking

import dev.pooq.ichor.gaia.extensions.buffer
import dev.pooq.ichor.gaia.extensions.short
import dev.pooq.ichor.gaia.extensions.string
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.SHORT
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.VAR_INT
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

data class Handshake(
  override val id: Int,
  override val state: State,
  val protocolVersion: Int,
  val serverAddress: String,
  val serverPort: Short,
  val nextState: NextState
) : ServerPacket() {

  override fun serialize(): ByteBuffer {
    return buffer(INT + VAR_INT + serverAddress.length + SHORT + VAR_INT){
      varInt(id)
      varInt(protocolVersion)
      string(serverAddress)
      short(serverPort)
      varInt(nextState.stateId)
    }
  }

  enum class NextState(
    val stateId: Int
  ){
    STATUS(1),
    LOGIN(2);

    companion object{
      fun byStateId(id: Int): NextState {
        return when(id){
          1 -> STATUS
          2 -> LOGIN
          else -> throw IllegalArgumentException("Entered State id must be 1 (Status) or 2 (Login).")
        }
      }
    }
  }
}