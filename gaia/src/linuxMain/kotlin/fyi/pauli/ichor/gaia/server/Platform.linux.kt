package fyi.pauli.ichor.gaia.server

import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level

/**
 * @author btwonion
 * @since 02/12/2023
 */
internal actual object Platform {
	actual fun setupPlatform() {
		KotlinLoggingConfiguration.logLevel = Level.DEBUG
	}
}