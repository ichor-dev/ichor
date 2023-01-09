package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.Packet
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusResponse
import dev.pooq.ichor.gaia.networking.packet.server.handshaking.Handshake
import java.nio.ByteBuffer
import java.util.*

enum class ClientPackets(
  val id: Int,
  val deserializer: ClientPacket.PacketDeserializer<*>
){


  //State = Status
  STATUS_RESPONSE(0x00, StatusResponse),

  ;

  companion object {
    fun deserialize(byteBuffer: ByteBuffer): ClientPacket? {
      val id = byteBuffer.varInt()
      return values().firstOrNull { it.id == id}
        ?.deserializer
        ?.deserialize(id, byteBuffer)
    }
  }
}
enum class ServerPackets(
val id: Int,
val serializer: ServerPacket.PacketSerializer<*>
){

  //State = Handshaking
  HANDSHAKE(0x00, Handshake),

  ;

  companion object {
    fun serialize(packet: ServerPacket): ByteBuffer? {
      return values()
        .firstOrNull { it.id == packet.id}
        ?.serializer
        ?.serialize(packet) //<- Error here
    }
  }
}