package dev.pooq.ichor.gaia.extensions

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.Theme
import com.github.ajalt.mordant.terminal.Terminal

val Terminal.errorStyle: TextStyle
	get() = (brightRed + bold)

val Terminal.warnStyle: TextStyle
	get() = (brightYellow + bold)

val Terminal.logStyle: TextStyle
	get() = (brightGreen + bold)

fun Terminal.error(message: Any? = null, error: Throwable) {
	this.println(errorStyle("[ERROR]") + white(" > ") + red("${message.let { "${it ?: ""}: " }}${error.stackTraceToString()}"))
}

fun Terminal.warn(message: Any?) {
	this.println(warnStyle("[WARN]") + white("  > ") + yellow(message.toString()))
}

fun Terminal.info(message: Any) {
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
