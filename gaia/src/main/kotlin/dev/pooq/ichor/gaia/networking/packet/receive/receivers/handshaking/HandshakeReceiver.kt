package dev.pooq.ichor.gaia.networking.packet.receive.receivers.handshaking

import com.github.ajalt.mordant.rendering.TextColors
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.server.Server

class HandshakeReceiver : PacketReceiver<Handshake> {

	override suspend fun onReceive(packet: Handshake, packetHandle: PacketHandle, server: Server) {
		terminal.debug(
			"""
          ${TextColors.magenta("--- Handshake Receiver ---")}
                    ${TextColors.cyan("Socket: ${packetHandle.connection.socket.remoteAddress}")}
                    ${TextColors.red("Old state: ${packetHandle.state}")}
                    ${TextColors.green("New state: ${packet.nextState}")}
                    ${TextColors.magenta("-----------------------")}
      """.trimIndent()
		)

		packetHandle.state = packet.nextState
	}
}
