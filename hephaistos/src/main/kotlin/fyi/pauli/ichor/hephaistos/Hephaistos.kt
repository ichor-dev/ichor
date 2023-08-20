package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.gaia.server.ichor
import fyi.pauli.ichor.hephaistos.networking.extensions.NetworkingExtensions
import io.github.oshai.kotlinlogging.KLogger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.dsl.lazyModule

@OptIn(KoinExperimentalAPI::class)
suspend fun main() = ichor(Hephaistos()) {
	startKoin {
		lazyModule {
			single<Hephaistos> { this@ichor }
			single<KLogger> { this@ichor.logger }
		}
	}

	NetworkingExtensions.initiateVanillaNetworking()
}

class Hephaistos : Server("Hephaistos") {
	override val httpClient: HttpClient = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(Constants.json)
		}
	}

	override suspend fun startup() {
		logger.info {
			"Starting server..."
		}


	}

	override suspend fun shutdown() {
		logger.info {
			"Shutdown server"
		}
	}
}