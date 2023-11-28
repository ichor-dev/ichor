package fyi.pauli.ichor.hephaistos.networking.receivers.status

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import fyi.pauli.ichor.gaia.config.ServerConfig
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public object StatusRequestReceiver : PacketReceiver<StatusRequest>, KoinComponent {
	private val serverConfig: ServerConfig by inject()

	override suspend fun onReceive(
		packet: StatusRequest,
		packetHandle: PacketHandle,
		server: Server
	) {
		packetHandle.sendPacket(StatusResponse(json.encodeToString(ServerPreview())))
	}

	@Serializable
	public class ServerPreview(
		public var version: Version = Version(),
		public var players: Players = Players(),
		public var description: Description = Description(),
		public var favicon: String = "data:image/png;base64,${serverConfig.server.base64FavIcon()}",
		public var enforceSecureChat: Boolean = true,
		public var previewsChat: Boolean = true
	) {

		@Serializable
		public class Version(
			public var name: String = "1.20.2",
			public var protocol: Int = 764
		)

		@Serializable
		public class Players(
			public var max: Int = 100,
			public var online: Int = 0,
			public var sample: List<PreviewPlayer> = listOf()
		) {

			@Serializable
			public class PreviewPlayer(
				public var name: String = "btwonion",
				@Contextual public var id: Uuid = uuidFrom("84c7eef5-ae2c-4ebb-a006-c3ee07643d79")
			) {

				public companion object {
					public fun fromPlayer(player: Player): PreviewPlayer = PreviewPlayer(player.profile.username, player.profile.uuid)
				}
			}
		}

		@Serializable
		public class Description(
			public var text: String = ""
		)
	}
}