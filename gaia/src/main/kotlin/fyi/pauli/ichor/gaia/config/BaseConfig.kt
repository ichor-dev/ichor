package fyi.pauli.ichor.gaia.config

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.TomlOutputConfig
import fyi.pauli.ichor.gaia.extensions.env
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

@Transient
internal val configToml = Toml(
	TomlInputConfig(
		ignoreUnknownNames = true,
		allowEmptyValues = true,
		allowNullValues = true,
		allowEmptyToml = false,
		allowEscapedQuotesInLiteralStrings = true
	), TomlOutputConfig(
		indentation = TomlIndentation.TWO_SPACES,
		allowEscapedQuotesInLiteralStrings = true,
		ignoreNullValues = false,
		ignoreDefaultValues = false,
		explicitTables = true
	)
)

@Serializable
data class BaseConfig(
	val environment: Map<String, String> = emptyMap(), val server: Server = Server()
) {

	companion object {
		internal fun loadConfig(): BaseConfig {
			val file = Path(env["BASE_CONFIG_PATH"] ?: "./config.toml")
			if (file.notExists() || file.readText().isBlank()) {
				val baseConfig = BaseConfig()
				if (file.notExists()) file.createFile()
				file.writeText(configToml.encodeToString(baseConfig))
				return baseConfig
			}

			return configToml.decodeFromString<BaseConfig>(file.readText())
		}
	}

	init {
		environment.filter { it.key != "BASE_CONFIG_PATH" }.forEach { (key, value) -> System.getenv()[key] = value }
	}

	@Serializable
	data class Server(
		val host: String = env["SERVER_HOST"] ?: "127.0.0.1",
		val port: String = env["SERVER_PORT"] ?: "25565",
		val favIconPath: String = "./favicon.png",
		val maxPacketSize: Int = 2_097_151
	) {

		fun base64FavIcon(): String {
			val path: Path = Paths.get("./", favIconPath)

			if (path.notExists()) return ""

			return path.readBytes().encodeBase64()
		}
	}
}