package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import java.nio.ByteBuffer

enum class ClientPackets(
  val id: Int,
  val deserializer: ClientPacket.PacketDeserializer<*>
){

  //State = Handshake
  HANDSHAKE(0x00, dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake.StatusRequest),

  //State = Status
  STATUS_REQUEST(0x00, StatusRequest),

  ;

  companion object {
    fun deserialize(byteBuffer: ByteBuffer): ClientPacket {
      val id = byteBuffer.varInt()
      return values().first { it.id == id}
        .deserializer
        .deserialize(byteBuffer)
    }
  }
}