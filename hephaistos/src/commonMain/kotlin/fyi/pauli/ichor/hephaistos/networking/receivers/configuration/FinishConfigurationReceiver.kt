package fyi.pauli.ichor.hephaistos.networking.receivers.configuration

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.configuration.FinishConfiguration
import fyi.pauli.ichor.gaia.server.Server

public object FinishConfigurationReceiver : PacketReceiver<FinishConfiguration> {
	override suspend fun onReceive(packet: FinishConfiguration, packetHandle: PacketHandle, server: Server) {
		packetHandle.state = State.PLAY
	}
}