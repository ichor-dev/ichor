package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.networking.IncomingPacket.PacketDeserializer
import fyi.pauli.ichor.gaia.networking.PacketIdentifier
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver

object PacketRegistry {
	val incomingPackets = mutableListOf<RegisteredIncomingPacket>()
}

data class RegisteredIncomingPacket(
	val identifier: PacketIdentifier,
	var deserializer: PacketDeserializer<*>,
	val receivers: MutableList<PacketReceiver<*>>
)