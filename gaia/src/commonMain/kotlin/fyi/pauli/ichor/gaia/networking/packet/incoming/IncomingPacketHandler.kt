package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.configuration.*
import fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginAcknowledged
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.PluginMessageResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.PingRequest
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.StatusRequest
import fyi.pauli.ichor.gaia.networking.serialization.RawPacket
import fyi.pauli.ichor.gaia.server.Server
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

public object IncomingPacketHandler {
	@OptIn(InternalSerializationApi::class)
	internal suspend fun deserializeAndHandle(rawPacket: RawPacket, packetHandle: PacketHandle, server: Server) {
		if (rawPacket is RawPacket.NotFound) {
			server.logger.warn {
				"Cannot find packet with length ${rawPacket.length}."
			}
			return
		}
		rawPacket as RawPacket.Found

		val clientPacket =
			PacketRegistry.incomingPackets.firstOrNull { it.identifier.id == rawPacket.id && it.identifier.state == packetHandle.state }
				?: error("Cannot find packet with id ${rawPacket.id} in state ${packetHandle.state} (Socket: ${packetHandle.connection.socket.remoteAddress})")

		server.logger.debug {
			"RECEIVED packet ${clientPacket.identifier.debuggingName} with id ${rawPacket.id} in state ${packetHandle.state.debugName} (Socket: ${packetHandle.connection.socket.remoteAddress})"
		}

		val packet = server.mcProtocol.decodeFromByteArray(clientPacket.kClass.serializer(), rawPacket.data)

		clientPacket.receivers.forEach { (_, receiver) ->
			receiver.onReceive(packet, packetHandle, server)
		}
	}

	public fun registerJoinPackets() {
		fun createPacket(
			state: State, id: Int, name: String, kClass: KClass<out IncomingPacket>
		): RegisteredIncomingPacket = RegisteredIncomingPacket(
			PacketIdentifier(id, state, name), kClass, mutableMapOf()
		)

		fun createLoginPacket(
			id: Int, name: String, kClass: KClass<out IncomingPacket>
		): RegisteredIncomingPacket = createPacket(State.LOGIN, id, name, kClass)

		fun createConfigurationPacket(
			id: Int, name: String, kClass: KClass<out IncomingPacket>
		) = createPacket(State.CONFIGURATION, id, name, kClass)

		val handshakePackets = listOf(
			createPacket(State.HANDSHAKING, 0x00, "Handshake", Handshake::class)
		)

		val statusPackets = listOf(
			createPacket(State.STATUS, 0x00, "Status Request", StatusRequest::class),
			createPacket(State.STATUS, 0x01, "Ping Request", PingRequest::class)
		)

		val loginPackets = listOf(
			createLoginPacket(0x00, "Login Start", LoginStart::class),
			createLoginPacket(0x01, "Encryption Response", EncryptionResponse::class),
			createLoginPacket(0x02, "Plugin Message Response", PluginMessageResponse::class),
			createLoginPacket(0x03, "Login Acknowledged", LoginAcknowledged::class)
		)

		val configurationPackets = listOf(
			createConfigurationPacket(0x00, "Plugin Message", PluginMessage::class),
			createConfigurationPacket(0x01, "Finish Configuration", FinishConfiguration::class),
			createConfigurationPacket(0x02, "Keep Alive", KeepAlive::class),
			createConfigurationPacket(0x03, "Pong", Pong::class),
			createConfigurationPacket(0x04, "Resource Pack Response", ResourcePackResponse::class)
		)

		PacketRegistry.incomingPackets.addAll(
			listOf(
				handshakePackets, statusPackets, loginPackets, configurationPackets
			).flatten()
		)
	}
}