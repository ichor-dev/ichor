package fyi.pauli.ichor.gaia.config

import io.ktor.util.*
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.notExists
import kotlin.io.path.readBytes


@Serializable
data class ServerConfig(
	val server: Server = Server()
)

@Serializable
data class Server(
	val host: String = System.getenv()["SERVER_HOST"] ?: "127.0.0.1",
	val port: String = System.getenv()["SERVER_PORT"] ?: "25565",
	val maxPacketSize: Int = System.getenv()["MAX_PACKET_SIZE"]?.toIntOrNull() ?: 2_097_151,
	val favIconPath: String = System.getenv()["FAV_ICON_PATH"] ?: "./favicon.png",
) {

	fun base64FavIcon(): String {
		val path: Path = Path(favIconPath)

		if (path.notExists()) return ""

		return path.readBytes().encodeBase64()
	}
}
