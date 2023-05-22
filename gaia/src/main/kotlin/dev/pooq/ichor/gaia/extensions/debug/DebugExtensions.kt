package dev.pooq.ichor.gaia.extensions.debug

import com.github.ajalt.mordant.rendering.TextColors.blue
import com.github.ajalt.mordant.rendering.TextColors.white
import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.extensions.debugStyle
import dev.pooq.ichor.gaia.extensions.env

var debug: Boolean = env("debug").toBoolean()

fun Terminal.debug(message: Any, force: Boolean = false) {
  if (!debug && !force) return
  this.println(debugStyle("[DEBUG]") + white(" > ") + blue(message.toString()))
}
