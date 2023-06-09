package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextColors.red
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import java.net.SocketException
import java.nio.ByteBuffer

suspend fun main(args: Array<String>) {
	Hephaistos.startup(args)
}

object Hephaistos : Server() {

	override suspend fun startup(args: Array<String>) {

		terminal.info(brightGreen("Starting server..."))

		terminal.debug("Startup arguments:")

		args.forEach { terminal.debug(it) }

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

		terminal.info(brightGreen("Server started successfully!"))

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
						terminal.info("Error in channel: ${e.stackTraceToString()}")
				} finally {
					connection.input.cancel()
					connection.output.close()
				}
			}
		}
	}

	override suspend fun shutdown() {
		terminal.info(red("Shutdown"))
	}
}