package fyi.pauli.ichor.hephaistos.networking.receivers.status

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.PingRequest
import fyi.pauli.ichor.gaia.networking.packet.outgoing.status.PingResponse
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
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