package fyi.pauli.ichor.hephaistos.networking.extensions

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.ichor.hephaistos.networking.receivers.HephaistosReceiverHelper

object NetworkingExtensions {
	fun initiateVanillaNetworking() {
		IncomingPacketHandler.registerJoinPackets()

		HephaistosReceiverHelper.registerVanillaJoinReceivers()
	}
}