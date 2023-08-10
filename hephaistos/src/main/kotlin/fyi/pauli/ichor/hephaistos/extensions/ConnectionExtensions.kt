package fyi.pauli.ichor.hephaistos.extensions

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.network.sockets.*
import java.nio.ByteBuffer

internal suspend fun Connection.handleIncoming(handle: PacketHandle, server: Server) {
	while (!input.isClosedForRead) {
		var size = 0
		var i = 0
		var b: Byte
		do {
			b = input.readByte()
			size = size or ((b.toInt() and 0x7F) shl (i * 7))
			i++
		} while ((b.toInt() and 0x80) != 0 && i < 5)

		val buffer = ByteBuffer.allocate(size)
		input.readAvailable(buffer)
		buffer.flip()

		IncomingPacketHandler.deserializeAndHandle(buffer, handle, server)
	}
}