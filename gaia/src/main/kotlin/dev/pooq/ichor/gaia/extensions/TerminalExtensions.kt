package dev.pooq.ichor.gaia.extensions

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.terminal.Terminal

fun Terminal.error(message: Any, error: Throwable){
  val errorStyle = (brightRed + bold)

  println(errorStyle("[ERROR]") + white(" > ") + red(message.toString()))
  println(errorStyle("[ERROR]") + white(" > ") + red(error.stackTraceToString()))
}

fun Terminal.log(message: Any){
  val logStyle = (brightGreen + bold)
  println(logStyle("[INF0]") + white("  > ") + brightWhite(message.toString()))
}
