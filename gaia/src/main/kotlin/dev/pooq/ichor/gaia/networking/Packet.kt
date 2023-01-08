package dev.pooq.ichor.gaia.networking

import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

interface Packet{
  val id: Int
  val state: State
}

abstract class ServerPacket : Packet {

  interface PacketSerializer<out T: ServerPacket>{
    fun serialize(packet: @UnsafeVariance T): ByteBuffer
  }
}

abstract class ClientPacket : Packet {

  interface PacketDeserializer<T: ClientPacket>{
    fun deserialize(id: Int, byteBuffer: ByteBuffer): T
  }
}
