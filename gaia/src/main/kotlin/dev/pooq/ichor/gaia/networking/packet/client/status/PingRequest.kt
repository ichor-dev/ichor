package dev.pooq.ichor.gaia.networking.packet.client.status

import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.status.PingResponse
import dev.pooq.ichor.gaia.server.Server
import java.nio.ByteBuffer

class PingRequest(
  val payload: Long
) : ClientPacket() {

  override val id: Int
    get() = 0x01

  override val state: State
    get() = State.STATUS

  companion object : PacketProcessor<PingRequest>(object : PacketReceiver<PingRequest>{
    override suspend fun onReceive(packet: PingRequest, packetHandle: PacketHandle, server: Server) {
      packetHandle.sendPacket(PingResponse(payload = packet.payload))
    }

  }) {
    override suspend fun deserialize(byteBuffer: ByteBuffer): PingRequest {
      return PingRequest(
        byteBuffer.long
      )
    }
  }
}