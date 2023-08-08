package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.extensions.bytes.decompress
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.IncomingPacket
import fyi.pauli.ichor.gaia.networking.PacketIdentifier
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.PingRequest
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.StatusRequest
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.receive.receivers.handshaking.HandshakeReceiver
import fyi.pauli.ichor.gaia.networking.packet.receive.receivers.login.LoginReceivers
import fyi.pauli.ichor.gaia.networking.packet.receive.receivers.status.PingRequestReceiver
import fyi.pauli.ichor.gaia.networking.packet.receive.receivers.status.StatusRequestReceiver
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

					State ${packetHandle.state.stateName}
					-----------------------
				""".trimIndent()
		}

		val deserializedPacket = clientPacket.deserializer.deserialize(buffer)

		clientPacket.receivers.forEach { it.onReceive(deserializedPacket, packetHandle, server) }
	}

	fun registerLoginPackets() {
		fun createPacket(
			state: State,
			id: Int,
			name: String,
			deserializer: IncomingPacket.PacketDeserializer<*>,
			vararg receivers: PacketReceiver<*>
		): RegisteredIncomingPacket =
			RegisteredIncomingPacket(PacketIdentifier(id, state, name), deserializer, receivers.toMutableList())

		fun createHandshakePacket(
			id: Int, name: String, deserializer: IncomingPacket.PacketDeserializer<*>, vararg receivers: PacketReceiver<*>
		): RegisteredIncomingPacket = createPacket(State.HANDSHAKING, id, name, deserializer, *receivers)

		fun createStatusPacket(
			id: Int, name: String, deserializer: IncomingPacket.PacketDeserializer<*>, vararg receivers: PacketReceiver<*>
		): RegisteredIncomingPacket = createPacket(State.STATUS, id, name, deserializer, *receivers)

		fun createLoginPacket(
			id: Int, name: String, deserializer: IncomingPacket.PacketDeserializer<*>, vararg receivers: PacketReceiver<*>
		): RegisteredIncomingPacket = createPacket(State.LOGIN, id, name, deserializer, *receivers)

		val handshakePackets = listOf(
			createHandshakePacket(0x00, "Handshake", Handshake, HandshakeReceiver)
		)

		val statusPackets = listOf(
			createStatusPacket(0x00, "Status Request", StatusRequest, StatusRequestReceiver),
			createStatusPacket(0x01, "Ping Request", PingRequest, PingRequestReceiver)
		)

		val loginPackets = listOf(
			createLoginPacket(0x00, "Login Start", LoginStart, LoginReceivers.LoginStartReceiver),
			createLoginPacket(0x01, "Encryption Response", EncryptionResponse, LoginReceivers.EncryptionResponseReceiver)
		)

		PacketRegistry.incomingPackets.addAll(listOf(handshakePackets, statusPackets, loginPackets).flatten())
	}
}