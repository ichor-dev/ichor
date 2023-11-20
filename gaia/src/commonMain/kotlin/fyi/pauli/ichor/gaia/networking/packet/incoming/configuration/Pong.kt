package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import fyi.pauli.prolialize.serialization.types.NumberType
import kotlinx.serialization.Serializable

/**
 * Response to the outgoing packet (Ping) with the same id.
 *
 * @param id id is the same as the ping packet
 */
@Serializable
public data class Pong(@NumberType var id: Int) : IncomingPacket