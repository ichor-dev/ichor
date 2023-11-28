package fyi.pauli.ichor.gaia.networking.packet.incoming.login

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

/**
 * Acknowledgement to the Login Success packet sent by the server.
 */
@Serializable
public class LoginAcknowledged : IncomingPacket