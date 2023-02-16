package dev.pooq.ichor.gaia.networking.packet

import com.github.ajalt.mordant.rendering.TextColors
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.decompress
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.Packet
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.client.login.LoginStart
import dev.pooq.ichor.gaia.networking.packet.client.status.PingRequest
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import dev.pooq.ichor.gaia.server.Server
import java.nio.ByteBuffer

enum class ClientPackets(
  val id: Int,
  val state: State,
  val processor: ClientPacket.PacketProcessor<*>
) {

  // State = Handshake
  HANDSHAKE(0x00, State.HANDSHAKING, Handshake),

  // State = Status
  STATUS_REQUEST(0x00, State.STATUS, StatusRequest),
  PING_REQUEST(0x01, State.STATUS, PingRequest),

  // State = Login
  LOGIN_START(0x00, State.LOGIN, LoginStart)

  ;

  companion object {
    suspend fun deserializeAndHandle(originalBuffer: ByteBuffer, packetHandle: PacketHandle, server: Server): Packet {
      val compression = packetHandle.compression

      val packetLength = originalBuffer.varInt()
      val dataLength = if (compression) originalBuffer.varInt() else packetLength

      var id: Int? = if (compression) null else originalBuffer.varInt()

      val buffer = if (compression) ByteBuffer.wrap(originalBuffer.array().decompress(dataLength)).apply {
        id = this.varInt()
      } else originalBuffer

      val clientPacket = values().first { clientPacket ->
        clientPacket.id == id && clientPacket.state == packetHandle.state
      }

      val processor = clientPacket.processor

      terminal.debug(
        """
          ${TextColors.magenta("--- Incoming packet ---")}
                    ${TextColors.cyan("PacketLength: $packetLength, DataLength: $dataLength, Compression: $compression")}
                    ${TextColors.yellow("Packet: $id")}
                    ${TextColors.yellow("State: ${packetHandle.state}")}
                    ${TextColors.green("Name: ${clientPacket.name}")}
                    ${TextColors.magenta("-----------------------")}
      """.trimIndent()
      )

      return processor.process(buffer, packetHandle, server)
    }
  }
}