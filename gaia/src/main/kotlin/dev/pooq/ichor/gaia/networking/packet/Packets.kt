package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusResponse
import dev.pooq.ichor.gaia.networking.packet.server.handshaking.Handshake
import java.nio.ByteBuffer

enum class ClientPackets(
  val id: Int,
  val deserializer: ClientPacket.PacketDeserializer<*>
){

  //State = Status
  STATUS_RESPONSE(0x00, StatusResponse),

  ;

  companion object {
    fun deserialize(byteBuffer: ByteBuffer): ClientPacket
    {
      val id = byteBuffer.varInt()
      return values().first { it.id == id}
        .deserializer
        .deserialize(id, byteBuffer)
    }
  }
}