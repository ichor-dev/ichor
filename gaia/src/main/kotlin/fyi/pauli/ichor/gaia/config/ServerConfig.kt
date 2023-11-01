package fyi.pauli.ichor.gaia.config

import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.notExists
import kotlin.io.path.readBytes

/**
 * Whole configuration section of the server.
 * Access it by [inject].
 * @property server
 * @author Paul Kindler
 * @since 30/10/2023
 */
@Serializable
data class ServerConfig(
	val server: Server = Server()
)

/**
 * Configuration section of the server.
 * Accessible through the server config
 * @property host host of the server.
 * @property port port on which the server listens.
 * @property maxPacketSize maximum packet size the client can send.
 * @property favIconPath path to the icon of the server.
 * @author Paul Kindler
 * @since 01/11/2023
 */
@Serializable
data class Server(
	val host: String = System.getenv()["SERVER_HOST"] ?: "127.0.0.1",
	val port: Int = System.getenv()["SERVER_PORT"]?.toIntOrNull() ?: 25565,
	val maxPacketSize: Int = System.getenv()["MAX_PACKET_SIZE"]?.toIntOrNull() ?: 2_097_151,
	val favIconPath: String = System.getenv()["FAV_ICON_PATH"] ?: "./favicon.png",
) {

	/**
	 * Generates [Base64](https://de.wikipedia.org/wiki/Base64) string representation of the server icon.
	 * @return [String] may be empty of the path does not exist.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	fun base64FavIcon(): String {
		val path: Path = Path(favIconPath)

		if (path.notExists()) return ""

		return path.readBytes().encodeBase64()
	}
}
