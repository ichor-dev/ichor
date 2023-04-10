import dev.pooq.ichor.gaia.networking.packet.State
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

suspend fun mainA() = coroutineScope {
  connect()
  repeat(10) {
    delay(2.seconds)
    sendHandshake(State.STATUS)
    println("sent!")
  }
}