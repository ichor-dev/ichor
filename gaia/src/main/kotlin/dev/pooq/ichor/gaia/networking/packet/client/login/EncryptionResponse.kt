package dev.pooq.ichor.gaia.networking.packet.client.login

import dev.pooq.ichor.gaia.extensions.bytes.byteArray
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.networking.packet.receive.receivers.login.LoginReceivers
import java.nio.ByteBuffer

class EncryptionResponse(
  val sharedSecretLength: Int,
  val sharedSecret: ByteArray,
  val verifyTokenLength: Int,
  val verifyToken: ByteArray
) : ClientPacket() {

  override val id: Int
    get() = 0x01

  override val state: State
    get() = State.LOGIN

  companion object : PacketProcessor<EncryptionResponse>(LoginReceivers.EncryptionResponseReceiver) {
    override suspend fun deserialize(byteBuffer: ByteBuffer): EncryptionResponse {
      val sharedSecretLength = byteBuffer.varInt()
      val sharedSecret = byteBuffer.byteArray(sharedSecretLength)
      val verifyTokenLength = byteBuffer.varInt()
      val verifyToken = byteBuffer.byteArray(verifyTokenLength)

      return EncryptionResponse(
        sharedSecretLength,
        sharedSecret,
        verifyTokenLength,
        verifyToken
      )
    }
  }
}