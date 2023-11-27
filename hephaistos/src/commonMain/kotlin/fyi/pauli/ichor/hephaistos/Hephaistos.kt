package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.gaia.server.serve
import fyi.pauli.ichor.hephaistos.networking.extensions.NetworkingExtensions
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking

public fun main(): Unit = runBlocking {
	serve(Hephaistos) {}
}

public object Hephaistos : Server("Hephaistos") {
	override val httpClient: HttpClient = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(Constants.json)
		}
	}

	override suspend fun startup() {
		logger.info {
			"Starting server..."
		}

		NetworkingExtensions.initiateVanillaNetworking()
	}

	override suspend fun shutdown() {
		logger.info {
			"Shutdown server"
		}
	}
}