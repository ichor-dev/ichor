package dev.pooq.ichor.gaia.networking.packet

import com.github.ajalt.mordant.rendering.TextColors
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.decompress
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.Packet
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.Handshake
import dev.pooq.ichor.gaia.networking.packet.client.handshaking.LegacyServerListPing
import dev.pooq.ichor.gaia.networking.packet.client.login.LoginStart
import dev.pooq.ichor.gaia.networking.packet.client.status.PingRequest
import dev.pooq.ichor.gaia.networking.packet.client.status.StatusRequest
import java.nio.ByteBuffer

enum class ClientPackets(
  val id: Int,
  val state: State,
  val processor: ClientPacket.PacketProcessor<*>
) {

  // State = Handshake
  HANDSHAKE(0x00, State.HANDSHAKING, Handshake),
  LEGACY_SERVER_LIST_PING(0xFE, State.HANDSHAKING, LegacyServerListPing),

  // State = Status
  STATUS_REQUEST(0x00, State.STATUS, StatusRequest),
  PING_REQUEST(0x01, State.STATUS, PingRequest),

  // State = Login
  LOGIN_START(0x00, State.LOGIN, LoginStart)

  ;

  companion object {
    suspend fun deserializeAndHandle(originalBuffer: ByteBuffer, packetHandle: PacketHandle): Packet {
      terminal.debug(TextColors.brightMagenta("--- Incoming packet ---"))

      val compression = packetHandle.compression

      val packetLength = originalBuffer.varInt()
      val dataLength = if (compression) originalBuffer.varInt() else packetLength

      var id: Int? = if (compression) null else originalBuffer.varInt()

      terminal.debug("PacketLength: $packetLength, DataLength: $dataLength, Compression: $compression")
      terminal.debug(TextColors.brightGreen("Packet: $id"))
      terminal.debug(TextColors.brightYellow("State: ${packetHandle.state}"))

      val buffer = if (compression) ByteBuffer.wrap(originalBuffer.array().decompress(dataLength)).apply {
        id = this.varInt()
      } else originalBuffer

      val clientPacket = values().first { clientPacket ->
        clientPacket.id == id && clientPacket.state == packetHandle.state
      }

      val processor = clientPacket.processor

      terminal.debug(TextColors.brightMagenta("-----------------------"))

      return processor.process(buffer, packetHandle)
    }
  }
}