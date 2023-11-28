package fyi.pauli.ichor.gaia.extensions.koin

import io.github.oshai.kotlinlogging.KLogger
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

internal class KoinLogger(private val logger: KLogger) : Logger() {

	override fun display(level: Level, msg: MESSAGE) {
		when (level) {
			Level.DEBUG -> logger.debug { msg }
			Level.INFO -> logger.info { msg }
			Level.WARNING -> logger.warn { msg }
			Level.ERROR -> logger.error { msg }
			Level.NONE -> return
		}
	}
}