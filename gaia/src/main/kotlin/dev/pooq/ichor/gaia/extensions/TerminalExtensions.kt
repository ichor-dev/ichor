package dev.pooq.ichor.gaia.extensions

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal

fun Terminal.error(message: Any, error: Throwable){
  val errorStyle = (brightRed + bold)

  this.println(errorStyle("[ERROR]") + white(" > ") + red(message.toString()))
  this.println(errorStyle("[ERROR]") + white(" > ") + red(error.stackTraceToString()))
}

fun Terminal.log(message: Any){
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
