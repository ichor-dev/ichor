package dev.pooq.ichor.gaia.networking.packet.receive.receivers.status

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.status.StatusResponse
import dev.pooq.ichor.gaia.server.Server

class StatusRequestReceiver : PacketReceiver<StatusRequest> {

  private val exampleResponse = """
    {
        "version": {
            "name": "1.19.4",
            "protocol": 762
        },
        "players": {
            "max": 420,
            "online": 69,
            "sample": [
                {
                    "name": "Paul",
                    "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
                }
            ]
        },
        "description": {
            "text": "Hello world"
        },
        "favicon": "data:image/png;base64,<data>",
        "enforcesSecureChat": true
    }
  """.trimIndent()

  override suspend fun onReceive(packet: StatusRequest, packetHandle: PacketHandle, server: Server) {
    packetHandle.sendPacket(StatusResponse(exampleResponse))
  }
}