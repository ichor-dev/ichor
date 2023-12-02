package fyi.pauli.ichor.gaia.networking.packet

import fyi.pauli.prolialize.serialization.types.EnumSerial
import kotlinx.serialization.Serializable

@Serializable
public enum class State(
	internal val debugName: String
) {
	@EnumSerial(0) HANDSHAKING("Handshaking"),
	@EnumSerial(1) STATUS("Status"),
	@EnumSerial(2) LOGIN("Login"),
	@EnumSerial(3) CONFIGURATION("Configuration"),
	@EnumSerial(4) PLAY("Play")
}