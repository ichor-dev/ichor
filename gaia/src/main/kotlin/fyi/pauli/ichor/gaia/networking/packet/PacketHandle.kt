package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.compress
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

class PacketHandle(
	var state: State,
	val connection: Connection,
	var threshold: Int = -1,
	var compression: Boolean = threshold > 0,
	val server: Server
) {

	suspend fun sendPacket(packet: OutgoingPacket) {
		withContext(server.coroutineContext) {
			launch {
				connection.output.write {
					it.put(packet.serialize().run { if (compression) ByteBuffer.wrap(it.array().compress()) else it.flip() })
				}

				server.logger.debug {
					"""
						--- Sent packet ---
						Packet: ${packet.id}
						Name: ${packet.javaClass.simpleName}
						-----------------------
					""".trimIndent()
				}
			}
		}
	}

	suspend fun handleIncoming(server: Server) {
		while (!connection.input.isClosedForRead) {
			var size = 0
			var i = 0
			var b: Byte
			do {
				b = connection.input.readByte()
				size = size or ((b.toInt() and 0x7F) shl (i * 7))
				i++
			} while ((b.toInt() and 0x80) != 0 && i < 5)

			val buffer = ByteBuffer.allocate(size)
			connection.input.readAvailable(buffer)
			buffer.flip()

			IncomingPacketHandler.deserializeAndHandle(buffer, this, server)
		}
	}
}