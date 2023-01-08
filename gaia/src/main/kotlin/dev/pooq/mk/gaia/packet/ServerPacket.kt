package dev.pooq.mk.gaia.packet

import java.nio.ByteBuffer

abstract class ServerPacket {

  abstract val id: Int
  abstract fun serialize(): ByteBuffer
}