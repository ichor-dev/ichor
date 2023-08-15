package fyi.pauli.ichor.hephaistos.networking.receivers.ocnfiguration

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.incoming.configuration.PluginMessage
import fyi.pauli.ichor.gaia.server.Server

object PluginMessageReceiver : PacketReceiver<PluginMessage> {
	@OptIn(ExperimentalStdlibApi::class)
	override suspend fun onReceive(packet: PluginMessage, packetHandle: PacketHandle, server: Server) {
		server.logger.debug {
			"Received Message in channel ${packet.channel} with data ${packet.data.toHexString().chunked(2)}"
		}
	}
}