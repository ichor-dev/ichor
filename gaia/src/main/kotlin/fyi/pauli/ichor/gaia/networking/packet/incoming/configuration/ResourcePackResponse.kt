package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.extensions.bytes.varInt
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import java.nio.ByteBuffer

/**
 * The response packet for loading the resourcepack.
 *
 * @param result The result received from the client.
 */
data class ResourcePackResponse(var result: ConfigurationResourcePackResult) : IncomingPacket() {
	companion object : PacketProcessor<ResourcePackResponse>() {
		override suspend fun deserialize(buffer: ByteBuffer): ResourcePackResponse {
			return ResourcePackResponse(ConfigurationResourcePackResult.entries.first { it.id == buffer.varInt() })
		}
	}

	enum class ConfigurationResourcePackResult(val id: Int) {
		LOADED(0), DECLINED(1), DOWNLOAD_FAILED(2), ACCEPTED(3)
	}
}