package fyi.pauli.ichor.gaia.networking

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.server.Server
import java.nio.ByteBuffer

interface Packet {
	val id: Int
	val state: State
}

abstract class ClientPacket : Packet {

	abstract class PacketProcessor<P : ClientPacket>(private vararg val receivers: PacketReceiver<P>) {
		protected abstract suspend fun deserialize(byteBuffer: ByteBuffer): P

		suspend fun process(byteBuffer: ByteBuffer, packetHandle: PacketHandle, server: Server): P {
			return deserialize(byteBuffer).apply {
				receivers.forEach { it.onReceive(this, packetHandle, server) }
			}
		}
	}
}

abstract class ServerPacket : Packet {
	abstract fun serialize(): ByteBuffer
}
