package fyi.pauli.ichor.hephaistos.networking.receivers.login

import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.models.nbt.builder.compoundTag
import fyi.pauli.ichor.gaia.models.payload.BrandPayload
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.FeatureFlags
import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.PluginMessage
import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.RegistryData
import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.UpdateTags
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.Disconnect
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.LoginSuccess
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.SetCompression
import fyi.pauli.ichor.gaia.networking.packet.receive.PacketReceiver
import fyi.pauli.ichor.gaia.server.Server
import fyi.pauli.ichor.hephaistos.Constants
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher

object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {
	val loginStartPackets = mutableMapOf<PacketHandle, LoginStart>()

	override suspend fun onReceive(
		packet: EncryptionResponse, packetHandle: PacketHandle, server: Server
	) {
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
		packetHandle.startConfiguration()
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
		packetHandle.sendPacket(LoginSuccess(userProfile))
	}

	private suspend fun PacketHandle.startConfiguration() {
		state = State.CONFIGURATION

		sendPacket(PluginMessage(BrandPayload(Constants.serverBrand)))
		sendPacket(FeatureFlags(mutableListOf()))
		sendPacket(RegistryData(compoundTag(null) {}))
		sendPacket(UpdateTags(mutableMapOf()))
	}
}