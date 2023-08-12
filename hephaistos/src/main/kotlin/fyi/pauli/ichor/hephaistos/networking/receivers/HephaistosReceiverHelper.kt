package fyi.pauli.ichor.hephaistos.networking.receivers

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.ichor.gaia.networking.packet.incoming.PacketRegistry
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.hephaistos.Constants
import fyi.pauli.ichor.hephaistos.networking.receivers.handshaking.HandshakeReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.login.LoginStartReceiver

object HephaistosReceiverHelper {
	val hephaistosReceiverIdentifier = Identifier(Constants.serverBrand, "vanilla-receiver")

	@Suppress("UNCHECKED_CAST")
	internal fun registerVanillaJoinReceivers() {
		fun registerReceiver(state: State, id: Int, vararg receivers: PacketReceiver<*>) {
			val registeredPacket =
				PacketRegistry.incomingPackets.first { it.identifier.state == state && it.identifier.id == id }

			receivers.map { it as PacketReceiver<IncomingPacket> }.toList().forEach {
				registeredPacket.receivers[hephaistosReceiverIdentifier] = it
			}
		}

		registerReceiver(State.HANDSHAKING, 0x00, HandshakeReceiver)

		registerReceiver(State.LOGIN, 0x00, LoginStartReceiver)
	}
}