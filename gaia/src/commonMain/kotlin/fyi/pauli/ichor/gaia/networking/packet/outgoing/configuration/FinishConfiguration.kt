package fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

/**
 * Sent by the server to notify the client that the configuration process has finished.
 * The client answers with its own Finish Configuration whenever it is ready to continue.
 *
 * In Vanilla, this packet switches the connection state to play.
 */
@Serializable
public class FinishConfiguration : OutgoingPacket() {
	override val id: Int
		get() = 0x02
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Finish Configuration"
}