package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.extensions.bytes.decompress
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
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
import fyi.pauli.ichor.gaia.server.Server
import java.nio.ByteBuffer

object IncomingPacketHandler {
	suspend fun deserializeAndHandle(originalBuffer: ByteBuffer, packetHandle: PacketHandle, server: Server) {
		val compression = packetHandle.compression

		val dataLength = if (compression) originalBuffer.varInt() else 0

		var id: Int? = if (compression) null else originalBuffer.varInt()

		val buffer = if (compression) ByteBuffer.wrap(originalBuffer.array().decompress(dataLength)).also {
			id = it.varInt()
		} else originalBuffer

		val clientPacket =
			PacketRegistry.incomingPackets.firstOrNull { it.identifier.id == id && it.identifier.state == packetHandle.state }
				?: error("Cannot find packet with id $id in state ${packetHandle.state}")

		server.logger.debug {
			"""
					--- Incoming packet ---
					Socket: ${packetHandle.connection.socket.remoteAddress}
					${if (compression) "DataLength: $dataLength, " else ""}Compression: $compression")}

					PacketId: $id
					PacketName: ${clientPacket.identifier.debuggingName}

					State ${packetHandle.state.debugName}
					-----------------------
				""".trimIndent()
		}

		val deserializedPacket = clientPacket.processor.deserialize(buffer)

		clientPacket.processor.invokeReceivers(
			deserializedPacket,
			clientPacket.receivers.values.toList(),
			packetHandle,
			server
		)
	}

	fun registerJoinPackets() {
		fun createPacket(
			state: State, id: Int, name: String, deserializer: IncomingPacket.PacketProcessor<*>
		): RegisteredIncomingPacket = RegisteredIncomingPacket(
			PacketIdentifier(id, state, name),
			deserializer,
			mutableMapOf()
		)

		fun createLoginPacket(
			id: Int, name: String, deserializer: IncomingPacket.PacketProcessor<*>
		): RegisteredIncomingPacket = createPacket(State.LOGIN, id, name, deserializer)

		fun createConfigurationPacket(
			id: Int, name: String, deserializer: IncomingPacket.PacketProcessor<*>
		) = createPacket(State.CONFIGURATION, id, name, deserializer)

		val handshakePackets = listOf(
			createPacket(State.HANDSHAKING, 0x00, "Handshake", Handshake)
		)

		val statusPackets = listOf(
			createPacket(State.STATUS, 0x00, "Status Request", StatusRequest),
			createPacket(State.STATUS, 0x01, "Ping Request", PingRequest)
		)

		val loginPackets = listOf(
			createLoginPacket(0x00, "Login Start", LoginStart),
			createLoginPacket(0x01, "Encryption Response", EncryptionResponse),
			createLoginPacket(0x02, "Plugin Message Response", PluginMessageResponse),
			createLoginPacket(0x03, "Login Acknowledged", LoginAcknowledged)
		)

		val configurationPackets = listOf(
			createConfigurationPacket(0x00, "Plugin Message", PluginMessage),
			createConfigurationPacket(0x01, "Finish Configuration", FinishConfiguration),
			createConfigurationPacket(0x02, "Keep Alive", KeepAlive),
			createConfigurationPacket(0x03, "Pong", Pong),
			createConfigurationPacket(0x04, "Resource Pack Response", ResourcePackResponse)
		)

		PacketRegistry.incomingPackets.addAll(
			listOf(
				handshakePackets, statusPackets, loginPackets, configurationPackets
			).flatten()
		)
	}
}