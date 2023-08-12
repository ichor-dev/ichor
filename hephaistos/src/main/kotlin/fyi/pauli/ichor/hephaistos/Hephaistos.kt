package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.extensions.handleIncoming
import io.github.oshai.kotlinlogging.KLogger
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.dsl.lazyModule
import java.net.SocketException

@OptIn(KoinExperimentalAPI::class)
suspend fun main() {
	val instance = Hephaistos()

	startKoin {
		lazyModule {
			single<Hephaistos> { instance }
			single<KLogger> { instance.logger }
		}
	}

	instance.startup()
}

class Hephaistos : Server("Hephaistos") {

	override suspend fun startup() {
		logger.info {
			"Starting server..."
		}

		val serverConfig = config.server

		val manager = SelectorManager(Dispatchers.IO)
		val serverSocket = aSocket(manager).tcp().bind(
			InetSocketAddress(
				serverConfig.host, serverConfig.port.toInt()
			)
		)

		logger.info {
			"Server started successfully!"
		}

		while (!serverSocket.isClosed) {
			val socket = serverSocket.accept()

			val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel(true))

			val handle = connection.handle()

			launch {
				try {
					handle.handleIncoming(this@Hephaistos)
				} catch (e: Throwable) {
					if (e !is ClosedReceiveChannelException && e !is SocketException) logger.error(e) {
						"Error in channel"
					}
				} finally {
					connection.input.cancel()
					connection.output.close()
				}
			}
		}
	}

	override suspend fun shutdown() {
		logger.info {
			"Shutdown server"
		}
	}
}