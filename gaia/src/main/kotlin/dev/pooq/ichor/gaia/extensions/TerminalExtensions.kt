package dev.pooq.ichor.gaia.extensions

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal

fun Terminal.error(message: Any? = null, error: Throwable) {
  val errorStyle = (brightRed + bold)

  this.println(errorStyle("[ERROR]") + white(" > ") + red("${message.let { "${it ?: ""}: " }}${error.stackTraceToString()}"))
}

fun Terminal.warn(message: Any?) {
  val logStyle = (brightYellow + bold)

  this.println(logStyle("[WARN]") + white("  > ") + yellow(message.toString()))
}

fun Terminal.log(message: Any) {
  val logStyle = (brightGreen + bold)

  this.println(logStyle("[INF0]") + white("  > ") + brightWhite(message.toString()))
}

val terminal = terminal()

fun terminal(
  theme: Theme = Theme.PlainAscii,
  tabWidth: Int = 8
): Terminal = Terminal(
  theme = theme,
  tabWidth = tabWidth
)
