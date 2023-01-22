package dev.pooq.ichor.gaia.networking

import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

interface Packet{
  val id: Int
  val state: State
}

abstract class ClientPacket : Packet {

  interface PacketDeserializer<P: ClientPacket>{
    fun deserialize(byteBuffer: ByteBuffer): P
  }
}

abstract class ServerPacket : Packet {

  abstract fun serialize(): ByteBuffer
}