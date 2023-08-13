package fyi.pauli.ichor.hephaistos.networking.receivers.ocnfiguration

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.configuration.PluginMessage
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.server.Server

object PluginMessageReceiver : PacketReceiver<PluginMessage> {
	override suspend fun onReceive(packet: PluginMessage, packetHandle: PacketHandle, server: Server) {
		server.logger.debug {
			"""
				--- Plugin Message Receiver ---
				Socket: ${packetHandle.connection.socket.remoteAddress}
				Identifier: ${packet.channel}
				-------------------------------
			""".trimIndent()
		}
	}
}