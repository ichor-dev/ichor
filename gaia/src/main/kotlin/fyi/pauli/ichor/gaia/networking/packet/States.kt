package fyi.pauli.ichor.gaia.networking.packet

enum class State(
	val stateName: String
) {
	HANDSHAKING("Handshaking"), STATUS("Status"), LOGIN("Login"), PLAY("Play")
}