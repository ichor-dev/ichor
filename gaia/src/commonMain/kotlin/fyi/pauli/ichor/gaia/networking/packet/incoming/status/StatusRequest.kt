package fyi.pauli.ichor.gaia.networking.packet.incoming.status

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

/**
 * The status can only be requested once immediately after the handshake, before any ping.
 * The server won't respond otherwise.
 */
@Serializable
public class StatusRequest : IncomingPacket