package dev.pooq.ichor.gaia.extensions.debug

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.extensions.env

var debug: Boolean = env("debug").toBoolean()

fun Terminal.debug(message: Any) {
  if (!debug) return
  val debugStyle = (brightYellow + bold)
  this.println(debugStyle("[DEBUG]") + white(" > ") + yellow(message.toString()))
}
