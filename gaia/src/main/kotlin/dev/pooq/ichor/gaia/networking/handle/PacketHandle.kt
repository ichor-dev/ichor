package dev.pooq.ichor.gaia.networking.handle

import dev.pooq.ichor.gaia.extensions.compress
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext

class PacketHandle(
  var state: State,
  val socket: Socket,
  var threshhold: Int = -1,
  var compression: Boolean = threshhold < 0,
  val coroutineContext: CoroutineContext
) {

  suspend fun sendPacket(packet: ServerPacket) {
    withContext(coroutineContext) {
      launch {
        socket.openWriteChannel(true).writeAvailable(
          packet.serialize().run {
            if (compression && limit() >= threshhold) ByteBuffer.wrap(array().compress()) else this
          }
        )
      }
    }
  }
}
