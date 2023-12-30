package fyi.pauli.ichor.gaia.config

import io.ktor.util.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.Serializable

/**
 * Whole configuration section of the server.
 * Access it by [org.koin.core.component.inject].
 * @property server
 * @author Paul Kindler
 * @since 30/10/2023
 */
@Serializable
public data class ServerConfig(
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
public data class Server(
	val host: String = "127.0.0.1",
	val port: Int = 25565,
	val maxPacketSize: Int = 2_097_151,
	val favIconPath: String = "./ichor/favicon.png",
) {

	/**
	 * Generates [Base64](https://de.wikipedia.org/wiki/Base64) string representation of the server icon.
	 * @return [String] may be empty of the path does not exist.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	public fun base64FavIcon(): String {
		val path = Path(favIconPath)
		val fileSystem = SystemFileSystem
		if (!fileSystem.exists(path)) return "<data>"
		val source = fileSystem.source(path).buffered()

		return source.readByteArray().encodeBase64()
	}
}
