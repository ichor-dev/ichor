package fyi.pauli.ichor.gaia.config

import fyi.pauli.ichor.gaia.extensions.env
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.*

@Transient
internal val gaiaJson = Json {
	encodeDefaults = true
}

@Serializable
data class BaseConfig(
	val environment: Map<String, String> = emptyMap(), val server: Server = Server()
) {
	companion object {
		fun loadConfig(): BaseConfig {
			val file = Path(env["BASE_CONFIG_PATH"] ?: "./config.json")
			if (file.notExists() || file.readText().isBlank()) {
				val baseConfig = BaseConfig()
				if (file.notExists()) file.createFile()
				file.writeText(gaiaJson.encodeToString(baseConfig))
				return baseConfig
			}

			return gaiaJson.decodeFromString<BaseConfig>(file.readText())
		}
	}

	init {
		environment.filter { it.key != "BASE_CONFIG_PATH" }.forEach { (key, value) -> System.getenv()[key] = value }
	}

	@Serializable
	data class Server(
		val host: String = env["SERVER_HOST"] ?: "127.0.0.1",
		val port: String = env["SERVER_PORT"] ?: "25565",
		val maxPacketSize: Int = 2_097_151
	)
}