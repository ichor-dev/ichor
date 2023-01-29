package dev.pooq.ichor.gaia.extensions

import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal

val terminal = terminal()

fun terminal(
  theme: Theme = Theme.PlainAscii,
  tabWidth: Int = 8
): Terminal = Terminal(
  theme = theme,
  tabWidth = tabWidth
)