package fyi.pauli.ichor.hephaistos.networking.receivers

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.ichor.gaia.networking.packet.incoming.PacketRegistry
import fyi.pauli.ichor.hephaistos.Constants
import fyi.pauli.ichor.hephaistos.networking.receivers.configuration.FinishConfigurationReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.configuration.PluginMessageReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.handshaking.HandshakeReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.login.EncryptionResponseReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.login.LoginAcknowledgedReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.login.LoginStartReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.status.PingRequestReceiver
import fyi.pauli.ichor.hephaistos.networking.receivers.status.StatusRequestReceiver

object Receivers {
	private val identifier = Identifier(Constants.serverBrand, "vanilla-receiver")

	@Suppress("UNCHECKED_CAST")
	internal fun registerVanillaJoinReceivers() {
		fun registerReceiver(state: State, id: Int, vararg receivers: PacketReceiver<*>) {
			val registeredPacket =
				PacketRegistry.incomingPackets.first { it.identifier.state == state && it.identifier.id == id }

			receivers.map { it as PacketReceiver<IncomingPacket> }.toList().forEach {
				registeredPacket.receivers[identifier] = it
			}
		}

		registerReceiver(State.HANDSHAKING, 0x00, HandshakeReceiver)

		registerReceiver(State.STATUS, 0x00, StatusRequestReceiver)
		registerReceiver(State.STATUS, 0x01, PingRequestReceiver)

		registerReceiver(State.LOGIN, 0x00, LoginStartReceiver)
		registerReceiver(State.LOGIN, 0x01, EncryptionResponseReceiver)
		registerReceiver(State.LOGIN, 0x03, LoginAcknowledgedReceiver)

		registerReceiver(State.CONFIGURATION, 0x00, PluginMessageReceiver)
		registerReceiver(State.CONFIGURATION, 0x01, FinishConfigurationReceiver)
	}
}