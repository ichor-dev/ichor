package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.compress
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
					it.put(packet.serialize().run { if (compression) ByteBuffer.wrap(array().compress()) else this.flip() })
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
}