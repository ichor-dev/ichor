package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import kotlin.reflect.KClass

public object PacketRegistry {
	public val incomingPackets: MutableList<RegisteredIncomingPacket> = mutableListOf()
}

public data class RegisteredIncomingPacket(
	val identifier: PacketIdentifier,
	var kClass: KClass<out IncomingPacket>,
	val receivers: MutableMap<Identifier, PacketReceiver<IncomingPacket>>
)