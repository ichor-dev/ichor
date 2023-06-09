package dev.pooq.ichor.gaia.entity.player

import dev.pooq.ichor.gaia.entity.Entity
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import java.util.*

class Player(
	val uuid: UUID,
	val name: String,
	val handle: PacketHandle
) : Entity.Creature