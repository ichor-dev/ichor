package fyi.pauli.ichor.gaia.networking.packet.receive.receivers.status

import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.client.status.StatusRequest
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.server.status.StatusResponse
import fyi.pauli.ichor.gaia.server.Server
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class StatusRequestReceiver : PacketReceiver<StatusRequest> {

	override suspend fun onReceive(
		packet: StatusRequest,
		packetHandle: PacketHandle,
		server: Server
	) {
		packetHandle.sendPacket(StatusResponse(Json.encodeToString(ServerPreview()).trimIndent()))
	}

	@Serializable
	class ServerPreview(
		val version: Version = Version(),
		val players: Players = Players(),
		val description: Description = Description(),
		val favicon: String = "data:image/png;base64,<data>",
		val enforceSecureChat: Boolean = true
	) {

		@Serializable
		class Version(
			val name: String = "1.19.4",
			val protocol: Int = 762
		)

		@Serializable
		class Players(
			val max: Int = 100,
			val online: Int = 0,
			val sample: Array<PreviewPlayer> = emptyArray()
		) {

			@Serializable
			class PreviewPlayer(
				val name: String = "Notch",
				@Contextual val uuid: UUID = UUID.randomUUID()
			) {

				companion object {
					fun fromPlayer(player: Player) = PreviewPlayer(player.profile.name, player.profile.id)
				}
			}
		}

		@Serializable
		class Description(
			val text: String = "<red>Hello ihr da"
		)
	}
}