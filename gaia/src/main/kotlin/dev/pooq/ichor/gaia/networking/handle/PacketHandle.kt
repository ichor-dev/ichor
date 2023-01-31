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
  var compression: Boolean
) {

  suspend fun sendPacket(packet: ServerPacket) {
    MainScope().launch {
      val serialized = if(compression){
        ByteBuffer.wrap(packet.serialize().array().compress())
      } else packet.serialize()

      socket.openWriteChannel(true).writeAvailable(serialized)
    }

  }
}