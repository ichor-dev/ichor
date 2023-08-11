package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver

object PacketRegistry {
	val incomingPackets = mutableListOf<RegisteredIncomingPacket>()
}

data class RegisteredIncomingPacket(
	val identifier: PacketIdentifier,
	var processor: IncomingPacket.PacketProcessor<*>,
	val receivers: MutableMap<Identifier, PacketReceiver<IncomingPacket>>
)