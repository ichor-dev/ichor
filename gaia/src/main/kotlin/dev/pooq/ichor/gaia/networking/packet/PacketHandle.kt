package dev.pooq.ichor.gaia.networking.packet

import com.github.ajalt.mordant.rendering.TextColors
import dev.pooq.ichor.gaia.extensions.compress
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.ServerPacket
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext

class PacketHandle(
  var state: State,
  val connection: Connection,
  var threshold: Int = -1,
  var compression: Boolean = threshold > 0,
  val coroutineContext: CoroutineContext
) {

  suspend fun sendPacket(packet: ServerPacket) {
    withContext(coroutineContext) {
      launch {
        connection.output.writeAvailable(packet.serialize().run {
          if (compression && limit() >= threshold) ByteBuffer.wrap(array().compress()) else this
        })
        terminal.debug(
          """
            ${TextColors.brightMagenta("--- Sent packet ---")}
                      ${TextColors.brightGreen("Packet: ${packet.id}")}
                      ${
            TextColors.brightYellow(
              "Name: ${
                ClientPackets.values().first { it.id == packet.id && it.state == packet.state }.name
              }"
            )
          }
                      ${TextColors.brightMagenta("-----------------------")}
          """.trimIndent()
        )
      }
    }
  }
}
