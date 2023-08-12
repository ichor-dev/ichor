package fyi.pauli.ichor.hephaistos.networking.extensions

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.gaia.server.Server
import java.nio.ByteBuffer

internal suspend fun PacketHandle.handleIncoming(server: Server) {
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