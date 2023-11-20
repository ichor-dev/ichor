package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.prolialize.serialization.types.EnumSerial
import kotlinx.serialization.Serializable

/**
 * The response packet for loading the resourcepack.
 *
 * @param result The result received from the client.
 */
@Serializable
public data class ResourcePackResponse(var result: ConfigurationResourcePackResult) : IncomingPacket {
	@Serializable
	public enum class ConfigurationResourcePackResult {
		@EnumSerial(0)
		LOADED,

		@EnumSerial(1)
		DECLINED,

		@EnumSerial(2)
		DOWNLOAD_FAILED,

		@EnumSerial(3)
		ACCEPTED
	}
}