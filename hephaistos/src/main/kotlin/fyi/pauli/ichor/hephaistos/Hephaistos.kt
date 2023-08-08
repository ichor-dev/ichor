package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.networking.packet.ClientPackets
import fyi.pauli.ichor.gaia.server.Server
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
import java.nio.ByteBuffer

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

		val server = config.server

		val manager = SelectorManager(Dispatchers.IO)
		val serverSocket =
			aSocket(manager).tcp().bind(
				InetSocketAddress(
					server.host, server.port.toInt()
				)
			)

		players.forEach {
			it.handle.connection.handle()
		}

		logger.info {
			"Server started successfully!"
		}

		while (!serverSocket.isClosed) {
			val socket = serverSocket.accept()

			val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel(true))

			val handle = connection.handle()

			launch {
				try {
					while (!connection.input.isClosedForRead) {
						var size = 0
						var i = 0
						var b: Byte
						do {
							b = connection.input.readByte()
							size = size or ((b.toInt() and 0x7F) shl (i * 7))
							i++
						} while ((b.toInt() and 0x80) != 0 && i < 5)

						val buffer = ByteBuffer.allocate(size)
						connection.input.readAvailable(buffer)
						buffer.flip()

						ClientPackets.deserializeAndHandle(buffer, handle, this@Hephaistos)
					}
				} catch (e: Throwable) {
					if (e !is ClosedReceiveChannelException && e !is SocketException)
						logger.error(e) {
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