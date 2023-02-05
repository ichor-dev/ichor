package dev.pooq.ichor.gaia.networking.handle

import dev.pooq.ichor.gaia.extensions.compress
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

class PacketHandle(
  var state: State,
  val socket: Socket,
  var threshhold: Int = -1,
  var compression: Boolean = threshhold < 0
) {

  suspend fun sendPacket(packet: ServerPacket) {
    MainScope().launch {
      socket.openWriteChannel(true).writeAvailable(
        packet.serialize().run {
          if(compression && limit() >= threshhold) ByteBuffer.wrap(array().compress()) else this
        }
      )
    }
  }
}
