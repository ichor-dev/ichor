package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextColors.red
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.env
import dev.pooq.ichor.gaia.extensions.log
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

object Hephaistos : Server() {

  override suspend fun startup(args: Array<String>) {
    terminal.log(brightGreen("Starting server..."))

    args.forEach { terminal.debug(it) }

    val manager = SelectorManager(Dispatchers.IO)
    val serverSocket =
      aSocket(manager).tcp().bind(
        InetSocketAddress(
          env("HEPHAISTOS_SERVER_ADDRESS") ?: "127.0.0.1",
          env("HEPHAISTOS_SERVER_PORT")?.toIntOrNull() ?: 25565
        )
      )

    terminal.log(brightGreen("Server started successfully!"))

    while (!serverSocket.isClosed) {
      val socket = serverSocket.accept()

      val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel(true))

      val client = socket.handle(connection)

      launch {
        try {
          while (!connection.input.isClosedForRead) {
            var value = 0
            var i = 0
            var b: Byte
            do {
              b = connection.input.readByte()
              value = value or ((b.toInt() and 0x7F) shl (i * 7))
              i++
            } while ((b.toInt() and 0x80) != 0 && i < 5)

            val buffer = ByteBuffer.allocate(value)
            connection.input.readAvailable(buffer)
            buffer.flip()

            ClientPackets.deserializeAndHandle(buffer, client, this@Hephaistos)
          }
        } catch (e: Throwable) {
          if (e !is ClosedReceiveChannelException)
            terminal.log("Error while reading packets: ${e.message}")
        } finally {
          connection.input.cancel()
          connection.output.close()
        }
      }
    }
  }

  override suspend fun shutdown() {
    terminal.log(red("Shutdown"))
  }
}

suspend fun main(args: Array<String>) {
  Hephaistos.startup(args)
}