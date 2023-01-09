package dev.pooq.ichor.gaia.networking

import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

interface Packet{
  val id: Int
  val state: State
}

abstract class ClientPacket : Packet {

  companion object

  interface PacketDeserializer<P: ClientPacket>{
    fun deserialize(id: Int, byteBuffer: ByteBuffer): P
  }
}

abstract class ServerPacket : Packet {

  interface PacketSerializer<P: ServerPacket>{
    fun serialize(packet: P): ByteBuffer
  }
}
