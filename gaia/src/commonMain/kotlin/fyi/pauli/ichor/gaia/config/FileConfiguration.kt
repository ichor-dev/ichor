package fyi.pauli.ichor.gaia.config

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.TomlOutputConfig
import fyi.pauli.ichor.gaia.extensions.internal.InternalGaiaApi
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

/**
 * Serializer used for server configuration.
 * @author Paul Kindler
 * @since 01/11/2023
 * @see Toml
 */
@InternalGaiaApi
public val configToml: Toml = Toml(
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
 * @property defaultConfig the data class of your configuration.
 * @throws IllegalArgumentException when config path is already used by another configuration.
 * @return file configuration either loaded from file or just the one you specified with [defaultConfig].
 * @author Paul Kindler
 * @since 30/10/2023
 */
@InternalGaiaApi
public inline fun <reified C> loadConfig(path: Path, defaultConfig: C): C {
	val fileSystem = SystemFileSystem
	val sink = fileSystem.sink(path).buffered()
	val source = fileSystem.source(path).buffered()

	val text = source.readString()

	if (text.isNotEmpty()) return try {
		configToml.decodeFromString<C>(text)
	} catch (e: Exception) {
		writeConfig(sink, defaultConfig)
	}

	return writeConfig(sink, defaultConfig)
}

@InternalGaiaApi
public inline fun <reified C> writeConfig(sink: Sink, config: C): C {
	val defaultText = configToml.encodeToString(config)
	sink.writeString(defaultText)
	return config
}