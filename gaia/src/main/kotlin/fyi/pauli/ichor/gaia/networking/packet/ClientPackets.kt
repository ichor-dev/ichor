package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.ichor.gaia.extensions.bytes.decompress
import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.ClientPacket
import fyi.pauli.ichor.gaia.networking.Packet
import fyi.pauli.ichor.gaia.networking.packet.client.handshaking.Handshake
import fyi.pauli.ichor.gaia.networking.packet.client.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.client.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.client.status.PingRequest
import fyi.pauli.ichor.gaia.networking.packet.client.status.StatusRequest
import fyi.pauli.ichor.gaia.server.Server
import java.nio.ByteBuffer

enum class ClientPackets(
	val id: Int,
	val state: State,
	val processor: ClientPacket.PacketProcessor<*>
) {

	// State = Handshake
	HANDSHAKE(0x00, State.HANDSHAKING, Handshake),

	// State = Status
	STATUS_REQUEST(0x00, State.STATUS, StatusRequest),
	PING_REQUEST(0x01, State.STATUS, PingRequest),

	// State = Login
	LOGIN_START(0x00, State.LOGIN, LoginStart),
	ENCRYPTION_RESPONSE(0x01, State.LOGIN, EncryptionResponse)
	;

	companion object {
		suspend fun deserializeAndHandle(
			originalBuffer: ByteBuffer,
			packetHandle: PacketHandle,
			server: Server
		): Packet {
			val compression = packetHandle.compression

			val dataLength = if (compression) originalBuffer.varInt() else 0

			var id: Int? = if (compression) null else originalBuffer.varInt()

			val buffer =
				if (compression) ByteBuffer.wrap(originalBuffer.array().decompress(dataLength)).also {
					id = it.varInt()
				} else originalBuffer

			val clientPacket = values().firstOrNull { clientPacket ->
				clientPacket.id == id && clientPacket.state == packetHandle.state
			} ?: error("Cannot find packet with id $id in state ${packetHandle.state}")

			server.logger.debug {
				"""
					--- Incoming packet ---
					Socket: ${packetHandle.connection.socket.remoteAddress}
					${if (compression) "DataLength: $dataLength, " else ""}Compression: $compression")}

					PacketId: $id
					PacketName: ${clientPacket.name}

					State ${packetHandle.state}
					-----------------------
				""".trimIndent()
			}

			return clientPacket.processor.process(buffer, packetHandle, server)
		}
	}
}