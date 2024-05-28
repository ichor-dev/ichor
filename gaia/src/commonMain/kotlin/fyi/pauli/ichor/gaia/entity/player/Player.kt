package fyi.pauli.ichor.gaia.entity.player

import fyi.pauli.ichor.gaia.entity.Entity
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle

/**
 * Default implementation for the player.
 * @property profile userprofile of the player.
 * @property handle connection handle of the player.
 * @author Paul Kindler
 * @since 01/11/2023
 */
public class Player(
	public val profile: UserProfile,
	public val handle: PacketHandle,
) : Entity.Creature

