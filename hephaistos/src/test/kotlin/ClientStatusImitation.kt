import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.extensions.log
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.hephaistos.Hephaistos
import io.ktor.utils.io.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

suspend fun main(): Unit = coroutineScope {
  connect()
  launch {
    try {
      while (!readChannel.isClosedForRead) {
        var size = 0
        var i = 0
        var b: Byte
        do {
          b = readChannel.readByte()
          size = size or ((b.toInt() and 0x7F) shl (i * 7))
          i++
        } while ((b.toInt() and 0x80) != 0 && i < 5)

        val buffer = ByteBuffer.allocate(size)
        readChannel.readAvailable(buffer)
        buffer.flip()

        val id = buffer.varInt()
        println(id)
        val string = buffer.string()
        println(string)
      }
    } catch (e: Throwable) {
      if (e !is ClosedReceiveChannelException)
        Hephaistos.terminal.log("Error while reading packets: ${e.stackTraceToString()}")
    } finally {
      readChannel.cancel()
      writeChannel.close()
    }
  }

  sendHandshake(State.STATUS)
  sendStatusRequest()
}