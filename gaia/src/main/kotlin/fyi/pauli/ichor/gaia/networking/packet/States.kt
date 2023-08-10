package fyi.pauli.ichor.gaia.networking.packet

enum class State(
	val debugName: String
) {
	HANDSHAKING("Handshaking"),
	STATUS("Status"),
	LOGIN("Login"),
	CONFIGURATION("Configuration"),
	PLAY("Play")
}