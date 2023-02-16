package dev.pooq.ichor.gaia.networking.packet.receive.receivers.login

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.login.EncryptionResponse
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.server.Server

object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {

  override suspend fun onReceive(packet: EncryptionResponse, packetHandle: PacketHandle, server: Server) {
    //TODO
  }
}