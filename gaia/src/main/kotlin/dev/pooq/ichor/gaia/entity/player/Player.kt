package dev.pooq.ichor.gaia.entity.player

import dev.pooq.ichor.gaia.entity.Entity
import dev.pooq.ichor.gaia.networking.packet.PacketHandle

class Player(
  val handle: PacketHandle
) : Entity.Creature
