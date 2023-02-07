package dev.pooq.ichor.gaia.extensions.debug

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.terminal.Terminal

var debug: Boolean = false

fun Terminal.debug(message: Any){
  if(!debug) return
  val debugStyle = (brightYellow + bold)
  println(debugStyle("[DEBUG]") + white(" > ") + yellow(message.toString()))
}
