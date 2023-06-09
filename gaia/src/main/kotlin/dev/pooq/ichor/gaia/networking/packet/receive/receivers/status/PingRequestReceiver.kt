package dev.pooq.ichor.gaia.networking.packet.receive.receivers.status

import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.status.PingRequest
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.status.PingResponse
import dev.pooq.ichor.gaia.server.Server
import io.ktor.utils.io.*

object PingRequestReceiver : PacketReceiver<PingRequest> {
	override suspend fun onReceive(packet: PingRequest, packetHandle: PacketHandle, server: Server) {
		packetHandle.sendPacket(PingResponse(packet.payload))
		packetHandle.connection.socket.close()
		packetHandle.connection.output.close()
		packetHandle.connection.input.cancel()
	}
}