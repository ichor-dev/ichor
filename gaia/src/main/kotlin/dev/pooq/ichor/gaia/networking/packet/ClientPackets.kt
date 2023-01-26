package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.client.Client
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.LegacyServerListPing
import dev.pooq.ichor.gaia.networking.packet.client.status.PingRequest
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import java.nio.ByteBuffer

enum class ClientPackets(
  val id: Int,
  val state: State,
  val deserializer: ClientPacket.PacketDeserializer<*>
){

  //State = Handshake
  HANDSHAKE(0x00, State.HANDSHAKING, Handshake),
  LEGACY_SERVER_LIST_PING(0xFE, State.HANDSHAKING, LegacyServerListPing),

  //State = Status
  STATUS_REQUEST(0x00, State.STATUS, StatusRequest),
  PING_REQUEST(0x01, State.STATUS, PingRequest),

  ;

  companion object {
    fun deserialize(byteBuffer: ByteBuffer, client: Client): ClientPacket {
      val length = byteBuffer.varInt()
      val id = byteBuffer.varInt()

      println("Received packet | ID: $id with length: $length")

      return values()
        .first { it.id == id && it.state == client.state }
        .deserializer
        .deserialize(byteBuffer)
    }
  }
}