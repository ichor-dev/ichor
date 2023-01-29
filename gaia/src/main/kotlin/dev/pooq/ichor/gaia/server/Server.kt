package dev.pooq.ichor.gaia.server

import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.networking.client.Client
import io.ktor.network.sockets.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
abstract class Server {

  init {
    Runtime.getRuntime().addShutdownHook(Thread {
      GlobalScope.launch {
        shutdown()
      }
    })
  }

  val clients: HashMap<ASocket, Client> = hashMapOf()

  fun terminal(
    theme: Theme = Theme.PlainAscii, tabWidth: Int = 8
  ): Terminal = Terminal(theme = theme, tabWidth = tabWidth)

  val terminal = terminal()

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()

}