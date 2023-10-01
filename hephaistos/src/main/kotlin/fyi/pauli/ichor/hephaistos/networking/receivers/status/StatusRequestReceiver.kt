package fyi.pauli.ichor.hephaistos.networking.receivers.status

import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.incoming.status.StatusRequest
import fyi.pauli.ichor.gaia.networking.packet.outgoing.status.StatusResponse
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.Constants.json
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.*

object StatusRequestReceiver : PacketReceiver<StatusRequest> {

	override suspend fun onReceive(
		packet: StatusRequest,
		packetHandle: PacketHandle,
		server: Server
	) {
		packetHandle.sendPacket(StatusResponse(json.encodeToString(ServerPreview()).trimIndent()))
	}

	@Serializable
	class ServerPreview(
		val version: Version = Version(),
		val players: Players = Players(),
		val description: Description = Description(),
		val favicon: String = "data:image/png;base64,<data>",
		val enforceSecureChat: Boolean = true,
		val previewsChat: Boolean = true
	) {
		@Serializable
		class Version(
			val name: String = "23w32a",
			val protocol: Int = 1073741969
		)

		@Serializable
		class Players(
			val max: Int = 100,
			val online: Int = 0,
			val sample: List<PreviewPlayer> = listOf()
		) {

			@Serializable
			class PreviewPlayer(
				val name: String = "Notch",
				@Contextual val uuid: UUID = UUID.randomUUID()
			) {

				companion object {
					fun fromPlayer(player: Player) = PreviewPlayer(player.profile.username, player.profile.uuid)
				}
			}
		}

		@Serializable
		class Description(
			val text: String = "Hello ihr da"
		)
	}
}