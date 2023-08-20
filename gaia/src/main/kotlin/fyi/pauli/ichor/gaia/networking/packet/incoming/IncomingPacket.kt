package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.server.Server
import java.nio.ByteBuffer

data class PacketIdentifier(val id: Int, val state: State, val debuggingName: String = "unnamed packet")

abstract class IncomingPacket {
	abstract class PacketProcessor<P : IncomingPacket> {
		internal abstract suspend fun deserialize(buffer: ByteBuffer): P

		@Suppress("UNCHECKED_CAST")
		internal suspend fun invokeReceivers(
			packet: IncomingPacket, receiver: List<PacketReceiver<*>>, handle: PacketHandle, server: Server
		) {
			packet as? P ?: error("incoming packet doesn't match the assigned packet!")

			receiver.map { it as PacketReceiver<P> }.forEach { it.onReceive(packet, handle, server) }
		}
	}
}