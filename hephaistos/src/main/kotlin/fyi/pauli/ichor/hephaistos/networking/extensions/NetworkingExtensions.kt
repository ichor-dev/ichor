package fyi.pauli.ichor.hephaistos.networking.extensions

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.hephaistos.networking.receivers.Receivers

object NetworkingExtensions {
	fun initiateVanillaNetworking() {
		IncomingPacketHandler.registerJoinPackets()

		Receivers.registerVanillaJoinReceivers()
	}
}