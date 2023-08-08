package fyi.pauli.ichor.gaia.networking.packet.receive.receivers.status

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.client.status.PingRequest
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.server.status.PingResponse
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.utils.io.*

object PingRequestReceiver : PacketReceiver<PingRequest> {
	override suspend fun onReceive(packet: PingRequest, packetHandle: PacketHandle, server: Server) {
		packetHandle.sendPacket(PingResponse(packet.payload))
		packetHandle.connection.socket.close()
		packetHandle.connection.output.close()
		packetHandle.connection.input.cancel()
	}
}