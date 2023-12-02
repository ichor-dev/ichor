package fyi.pauli.ichor.gaia.networking.packet.incoming.handshaking

import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.prolialize.serialization.types.NumberType
import fyi.pauli.prolialize.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.Serializable

@Serializable
public data class Handshake(
	val protocolVersion: Int,
	val serverAddress: String,
	@NumberType(MinecraftNumberType.UNSIGNED) val serverPort: Short,
	val nextState: State
) : IncomingPacket