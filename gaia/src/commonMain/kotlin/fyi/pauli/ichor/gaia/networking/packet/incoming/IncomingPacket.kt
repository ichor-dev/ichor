package fyi.pauli.ichor.gaia.networking.packet.incoming

import fyi.pauli.ichor.gaia.networking.packet.State

public data class PacketIdentifier(val id: Int, val state: State, val debuggingName: String = "unnamed packet")

public interface IncomingPacket