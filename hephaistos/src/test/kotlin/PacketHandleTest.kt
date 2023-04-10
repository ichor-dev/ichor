import dev.pooq.ichor.gaia.extensions.bytes.short
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.SHORT
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
  val socket = aSocket(SelectorManager(Dispatchers.IO)).tcp().connect("127.0.0.1", 25565)
  val writeChannel = socket.openWriteChannel(true)
  socket.openReadChannel()

  repeat(10) {
    delay(2.seconds)
    // Imitates sending a Handshake packet
    writeChannel.write {
      it.varInt(INT + INT + INT + "127.0.0.1".length + SHORT + INT)
      it.varInt(0x00)
      it.varInt(162)
      it.string("127.0.0.1")
      it.short(25565)
      it.varInt(1)
    }
    println("sent!")
  }
}