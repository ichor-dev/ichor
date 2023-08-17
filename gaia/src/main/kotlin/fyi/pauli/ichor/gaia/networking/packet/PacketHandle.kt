package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.build
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
					val rawPacket = packet.serialize()
					it.put(rawPacket.build(rawPacket.dataSize > threshold))
				}

				server.logger.debug {
					"Sent packet ${packet.debugName} with id ${packet.id}."
				}
			}
		}
	}
}