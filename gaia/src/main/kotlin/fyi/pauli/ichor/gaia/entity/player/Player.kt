package fyi.pauli.ichor.gaia.entity.player

import fyi.pauli.ichor.gaia.entity.Entity
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle

class Player(
	val profile: UserProfile,
	val handle: PacketHandle
) : Entity.Creature