package dev.pooq.ichor.gaia.networking.packet.receive.receivers.login

import dev.pooq.ichor.gaia.entity.player.UserProfile
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.packet.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.client.login.EncryptionResponse
import dev.pooq.ichor.gaia.networking.packet.client.login.LoginStart
import dev.pooq.ichor.gaia.networking.packet.receive.PacketReceiver
import dev.pooq.ichor.gaia.networking.packet.server.login.EncryptionRequest
import dev.pooq.ichor.gaia.networking.packet.server.login.LoginSuccess
import dev.pooq.ichor.gaia.networking.packet.server.login.SetCompression
import dev.pooq.ichor.gaia.server.Server
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
					"",
					server.encryptionPair.public.encoded.size,
					server.encryptionPair.public.encoded,
					server.verifyToken.size,
					server.verifyToken
				)
			)
		}
	}

	object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {
		override suspend fun onReceive(
			packet: EncryptionResponse,
			packetHandle: PacketHandle,
			server: Server
		) {
			// TODO kick player if he didn't send an login start packet
			val loginStartPacket = loginStartPackets[packetHandle]

			if (loginStartPacket == null) {
				terminal.debug("No login start packet sent!")
				return
			}

			terminal.debug("Authenticating ${loginStartPacket.name}...")

			val userProfile = requestUserProfile(server, loginStartPacket, packet)
			packetHandle.gameProfile = userProfile

			terminal.debug("${loginStartPacket.name} has uuid ${userProfile.id}")

			enableCompression(packetHandle)
			sendLoginSuccess(packetHandle, userProfile)
		}

		private suspend fun requestUserProfile(
			server: Server,
			loginStartPacket: LoginStart,
			encryptionResponsePacket: EncryptionResponse
		): UserProfile {
			val digest = MessageDigest.getInstance("SHA-1")
			digest.update("".toByteArray(Charsets.US_ASCII))
			digest.update(
				Cipher.getInstance(server.encryptionPair.private.algorithm)
					.also { it.init(Cipher.DECRYPT_MODE, server.encryptionPair.private) }
					.doFinal(encryptionResponsePacket.sharedSecret)
			)
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
				LoginSuccess(
					userProfile.id,
					userProfile.name,
					userProfile.properties.size,
					userProfile.properties
				)
			)
		}
	}
}
