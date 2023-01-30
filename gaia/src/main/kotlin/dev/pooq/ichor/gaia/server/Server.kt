package dev.pooq.ichor.gaia.server

import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.networking.client.Receiver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class Server {

  init {
    Runtime.getRuntime().addShutdownHook(Thread {
      MainScope().launch {
        shutdown()
      }
    })
  }

  val receivers: HashSet<Receiver> = hashSetOf()

  fun terminal(
    theme: Theme = Theme.PlainAscii, tabWidth: Int = 8
  ): Terminal = Terminal(theme = theme, tabWidth = tabWidth)

  val terminal = terminal()

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()

}