package dev.pooq.ichor.gaia.networking.packet.receive.receivers.status

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.status.StatusResponse
import dev.pooq.ichor.gaia.server.Server

class StatusRequestReceiver : PacketReceiver<StatusRequest> {

  override suspend fun onReceive(packet: StatusRequest, packetHandle: PacketHandle, server: Server) {
    packetHandle.sendPacket(StatusResponse("JSON HERE"))
  }
}