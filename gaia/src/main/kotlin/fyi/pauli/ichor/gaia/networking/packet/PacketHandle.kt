package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.build
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
					it.put(packet.serialize().build(compression))
				}

				server.logger.debug {
					"""

						----- Sent packet -----
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
			val size = run<Int> {
				var result = 0
				var shift = 0
				while (true) {
					val b: Byte = connection.input.readByte()
					result = result or (b.toInt() and 0x7f shl shift)
					if (b >= 0) return@run result
					shift += 7
				}
				result
			}

			val buffer = ByteBuffer.allocate(size)
			connection.input.readAvailable(buffer)
			buffer.flip()

			IncomingPacketHandler.deserializeAndHandle(buffer, this, server)
		}
	}
}