package fyi.pauli.ichor.gaia.config

import fyi.pauli.ichor.gaia.extensions.env
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

@Serializable
data class BaseConfig(
	val environment: Map<String, String> = emptyMap(),
	val server: fyi.pauli.ichor.gaia.config.BaseConfig.Server = fyi.pauli.ichor.gaia.config.BaseConfig.Server()
) {

	companion object {
		fun loadConfig(): fyi.pauli.ichor.gaia.config.BaseConfig {
			return Json.decodeFromString<fyi.pauli.ichor.gaia.config.BaseConfig>(
				Files.lines(Paths.get(env["BASE_CONFIG_PATH"] ?: "./config.toml"))
					.collect(Collectors.joining())
			)
		}
	}

	init {
		environment.filter { it.key != "BASE_CONFIG_PATH" }
			.forEach { (key, value) -> System.getenv()[key] = value }

	}

	@Serializable
	data class Server(
		val host: String = env["SERVER_HOST"] ?: "127.0.0.1",
		val port: String = env["SERVER_PORT"] ?: "25565"
	)
}