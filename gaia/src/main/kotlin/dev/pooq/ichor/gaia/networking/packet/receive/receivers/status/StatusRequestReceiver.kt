package dev.pooq.ichor.gaia.networking.packet.receive.receivers.status

import dev.pooq.ichor.gaia.entity.player.Player
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.status.StatusResponse
import dev.pooq.ichor.gaia.server.Server
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
					fun fromPlayer(player: Player) = PreviewPlayer(player.name, player.uuid)
				}
			}
		}

		@Serializable
		class Description(
			val text: String = "Hello ihr da"
		)
	}
}