package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.build
import fyi.pauli.ichor.gaia.extensions.bytes.buildCompressed
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import kotlin.experimental.and

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
				connection.output.writeFully(
					if (compression) packet.serialize().buildCompressed() else packet.serialize().build()
				)

				server.logger.debug { "SENT packet ${packet.debugName} with id ${packet.id} in state ${packet.state}. [Compression: $compression, Socket: ${connection.socket.remoteAddress}]" }
			}
		}
	}

	suspend fun handleIncoming(server: Server) {
		while (!connection.input.isClosedForRead) {
			val size = run<Int> {
				var i = 0
				var j = 0

				var b: Byte
				do {
					b = connection.input.readByte()
					i = i or (b.toInt() and 127 shl j++ * 7)
					if (j > 5) {
						throw RuntimeException("VarInt too big")
					}
				} while ((b and 128.toByte()).toInt() == 128)

				return@run i
			}

			val buffer = ByteBuffer.allocate(size)
			connection.input.readAvailable(buffer)
			buffer.flip()

			IncomingPacketHandler.deserializeAndHandle(buffer, this, server)
		}
	}
}