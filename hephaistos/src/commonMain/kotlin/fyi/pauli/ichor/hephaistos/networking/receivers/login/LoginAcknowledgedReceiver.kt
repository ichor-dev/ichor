package fyi.pauli.ichor.hephaistos.networking.receivers.login

import fyi.pauli.ichor.gaia.models.payload.BrandPayload
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginAcknowledged
import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.*
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.Constants
import fyi.pauli.nbterialize.extensions.buildCompoundTag

public object LoginAcknowledgedReceiver : PacketReceiver<LoginAcknowledged> {
	override suspend fun onReceive(packet: LoginAcknowledged, packetHandle: PacketHandle, server: Server) {
		packetHandle.state = State.CONFIGURATION

		packetHandle.startConfiguration()
	}

	private suspend fun PacketHandle.startConfiguration() {
		sendPacket(PluginMessage(BrandPayload(Constants.SERVER_BRAND)))
		sendPacket(FeatureFlags(mutableListOf()))
		sendPacket(RegistryData(buildCompoundTag {}))
		sendPacket(UpdateTags(mutableMapOf()))

		sendPacket(FinishConfiguration())
	}
}