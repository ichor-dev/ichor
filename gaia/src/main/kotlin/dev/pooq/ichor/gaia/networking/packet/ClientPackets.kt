package dev.pooq.ichor.gaia.networking.packet

import dev.pooq.ichor.gaia.extensions.decompress
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import java.nio.ByteBuffer

import dev.pooq.ichor.gaia.networking.Packet
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.LegacyServerListPing
import dev.pooq.ichor.gaia.networking.packet.client.status.PingRequest
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest

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
    suspend fun deserializeAndHandle(originalBuffer: ByteBuffer, packetHandle: PacketHandle) : Packet {
      val length = originalBuffer.varInt()

      val buffer = if(!packetHandle.compression) originalBuffer else ByteBuffer.wrap(originalBuffer.array().decompress(length))

      val id = buffer.varInt()

      val clientPacket = values().first { clientPacket ->
        clientPacket.id == id && clientPacket.state == packetHandle.state
      }

      val deserializer = clientPacket.deserializer

      return deserializer.deserializeAndHandle(buffer, packetHandle)
    }
  }
}