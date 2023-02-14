package dev.pooq.ichor.gaia.networking

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

interface Packet {
  val id: Int
  val state: State
}

abstract class ClientPacket : Packet {

  abstract class PacketProcessor<P : ClientPacket>(private vararg val handlers: PacketReceiver<P>) {
    protected abstract suspend fun deserialize(byteBuffer: ByteBuffer): P

    suspend fun process(byteBuffer: ByteBuffer, packetHandle: PacketHandle): P {
      return deserialize(byteBuffer).apply {
        handlers.forEach { handler -> handler.onReceive(this, packetHandle) }
      }
    }
  }
}

abstract class ServerPacket : Packet {
  abstract suspend fun serialize(): ByteBuffer
}
