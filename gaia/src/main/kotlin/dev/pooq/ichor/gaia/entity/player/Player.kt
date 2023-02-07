package dev.pooq.ichor.gaia.entity.player

import dev.pooq.ichor.gaia.entity.Entity
import dev.pooq.ichor.gaia.networking.handle.PacketHandle

class Player(
  val handle: PacketHandle
) : Entity.Creature
