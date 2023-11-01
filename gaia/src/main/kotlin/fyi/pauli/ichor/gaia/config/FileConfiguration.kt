package fyi.pauli.ichor.gaia.config

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.TomlOutputConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import kotlin.io.path.*

/**
 * Serializer used for server configuration.
 * @author Paul Kindler
 * @since 01/11/2023
 * @see Toml
 */
val configToml = Toml(
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

/**
 * Function to load and create file configuration of given config at given path.
 * You don't need to call this function because you can just use config(YourConfigClassInstance)
 * @param C the type of configuration it will return.
 * @property configuration the data class of your configuration.
 * @throws IllegalArgumentException when config path is already used by another configuration.
 * @return file configuration either loaded from file or just the one you specified with [configuration].
 * @author Paul Kindler
 * @since 30/10/2023
 */
inline fun <reified C> loadConfig(path: Path, configuration: C): C {
	val text = path.readText()

	if (path.exists() && (text.isNotBlank() || text.isNotEmpty())) return configToml.decodeFromString<C>(text)

	val toml = configToml.encodeToString(configuration)
	if (path.notExists() || text.isBlank()) {
		if (path.notExists()) path.createFile()
		path.appendText(toml)
	}

	if (path.exists() && !text.contains(toml)) {
		throw IllegalArgumentException("This config file is already in use by another configuration." +
			"Consider changing the file or delete the old configuration.")
	}

	return configToml.decodeFromString<C>(path.readText())
}