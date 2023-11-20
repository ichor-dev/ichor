package fyi.pauli.ichor.gaia.networking.packet.incoming.configuration

import fyi.pauli.ichor.gaia.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

/**
 * Sent by the client to notify the client that the configuration process has finished.
 * It is sent in response to the server's Finish Configuration.
 */
@Serializable
public class FinishConfiguration : IncomingPacket