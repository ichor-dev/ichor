package fyi.pauli.ichor.gaia.networking.packet.receive.receivers.login

import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.Disconnect
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.EncryptionRequest
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.LoginSuccess
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.SetCompression
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher

object LoginReceivers {
    var loginStartPackets = mutableMapOf<PacketHandle, LoginStart>()

    object LoginStartReceiver : PacketReceiver<LoginStart> {
        override suspend fun onReceive(packet: LoginStart, packetHandle: PacketHandle, server: Server) {
            loginStartPackets[packetHandle] = packet
            packetHandle.sendPacket(
                EncryptionRequest(
                    "", server.encryptionPair.public.encoded, server.verifyToken
                )
            )
        }
    }

    object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {
        override suspend fun onReceive(
            packet: EncryptionResponse, packetHandle: PacketHandle, server: Server
        ) {
            // TODO kick player if he didn't send an login start packet
            val loginStartPacket = loginStartPackets[packetHandle]
            if (loginStartPacket == null) {
                server.logger.debug {
                    "No login start packet sent!"
                }
                packetHandle.sendPacket(Disconnect("")) // TODO: Add kick reason
                return
            }

            server.logger.debug {
                "Authenticating ${loginStartPacket.name}..."
            }

            val userProfile = requestUserProfile(server, loginStartPacket, packet)
            val player = Player(userProfile, packetHandle)
            server.players.add(player)
            server.logger.debug {
                "${userProfile.username} has uuid ${userProfile.uuid}"
            }

            enableCompression(packetHandle)
            sendLoginSuccess(packetHandle, userProfile)
        }

        private suspend fun requestUserProfile(
            server: Server, loginStartPacket: LoginStart, encryptionResponsePacket: EncryptionResponse
        ): UserProfile {
            val digest = MessageDigest.getInstance("SHA-1")
            digest.update("".toByteArray(Charsets.US_ASCII))
            digest.update(Cipher.getInstance(server.encryptionPair.private.algorithm)
                .also { it.init(Cipher.DECRYPT_MODE, server.encryptionPair.private) }
                .doFinal(encryptionResponsePacket.sharedSecret))
            digest.update(server.encryptionPair.public.encoded)
            val hash = BigInteger(digest.digest()).toString(16)

            return server.httpClient.get(Url("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=${loginStartPacket.name}&serverId=$hash"))
                .body<UserProfile>()
        }

        private suspend fun enableCompression(packetHandle: PacketHandle) {
            packetHandle.sendPacket(SetCompression(1))
            packetHandle.threshold = 1
            packetHandle.compression = true
        }

        private suspend fun sendLoginSuccess(packetHandle: PacketHandle, userProfile: UserProfile) {
            packetHandle.sendPacket(
                LoginSuccess(userProfile)
            )
        }
    }
}
