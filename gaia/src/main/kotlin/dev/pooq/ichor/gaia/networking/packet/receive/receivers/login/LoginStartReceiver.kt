package dev.pooq.ichor.gaia.networking.packet.receive.receivers.login

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.login.LoginStart
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.login.EncryptionRequest
import dev.pooq.ichor.gaia.server.Server

object LoginStartReceiver : PacketReceiver<LoginStart> {

  override suspend fun onReceive(packet: LoginStart, packetHandle: PacketHandle, server: Server) {
    packetHandle.sendPacket(
      EncryptionRequest(
        "",
        server.encryptionPair.public.encoded.size,
        server.encryptionPair.public.encoded,
        server.verifyToken.size,
        server.verifyToken
      )
    )
  }
}