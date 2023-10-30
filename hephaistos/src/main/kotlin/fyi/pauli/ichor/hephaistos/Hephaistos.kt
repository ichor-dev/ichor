package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.config.ServerConfig
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.gaia.server.serve
import fyi.pauli.ichor.hephaistos.networking.extensions.NetworkingExtensions
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import kotlin.io.path.Path

suspend fun main() = try {
	serve(Hephaistos) {
		config(Path("./test.toml"), Test())
	}
} catch (e: Exception) {
	e.printStackTrace()
}

@Serializable
data class Test(
	val name: String = "Paul",
	val age: Int = 17
)

object Hephaistos : Server("Hephaistos") {

	override val httpClient: HttpClient = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(Constants.json)
		}
	}

	override suspend fun startup() {
		val config: ServerConfig by inject(ServerConfig::class.java)
		config.also { println(it) }
		val testConfig: Test by inject(Test::class.java)
		testConfig.also { println(it) }
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