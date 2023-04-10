package dev.pooq.ichor.gaia.networking.packet.server.login

import dev.pooq.ichor.gaia.entity.player.Property
import dev.pooq.ichor.gaia.extensions.bytes.*
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer
import java.util.*

class LoginSuccess(val uuid: UUID, val username: String, val propertiesCount: Int, val properties: List<Property>) :
  ServerPacket() {
  override fun serialize(): ByteBuffer {
    return compressedBuffer {
      varLong(uuid.mostSignificantBits)
      varLong(uuid.leastSignificantBits)

      string(username)
      varInt(propertiesCount)

      properties.forEach {
        string(it.name)
        string(it.value)
        boolean(true)
        string(it.signature)
      }
    }
  }

  override val id: Int
    get() = 0x02
  override val state: State
    get() = State.LOGIN
}