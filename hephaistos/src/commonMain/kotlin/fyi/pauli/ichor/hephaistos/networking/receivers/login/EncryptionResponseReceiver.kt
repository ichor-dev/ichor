package fyi.pauli.ichor.hephaistos.networking.receivers.login

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import dev.whyoleg.cryptography.algorithms.asymmetric.RSA
import fyi.pauli.ichor.gaia.entity.player.Player
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.networking.packet.PacketHandle
import fyi.pauli.ichor.gaia.networking.packet.PacketReceiver
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.ichor.gaia.networking.packet.incoming.login.LoginStart
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.Disconnect
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.LoginSuccess
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.SetCompression
import fyi.pauli.ichor.gaia.server.Server
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*

public object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {
	public val loginStartPackets: MutableMap<PacketHandle, LoginStart> = mutableMapOf()

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
		if (userProfile == null) {
			packetHandle.sendPacket(Disconnect("")) // TODO: Add kick reason
			return
		}

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
	): UserProfile? {
		val failed = server.encryptionPair.publicKey.encryptor().encrypt(encryptionResponsePacket.verifyToken).contentEquals(encryptionResponsePacket.verifyToken)
		if (failed) server.logger.error { "Encryption failed for player ${loginStartPacket.name} (${loginStartPacket.uuid})" }.also { return null }

		val digest = Digest("SHA-1")
		digest += "".toByteArray(Charset.forName("ISO_8859_1"))
		digest += server.encryptionPair.publicKey.encodeTo(RSA.PublicKey.Format.PEM)
		digest += encryptionResponsePacket.sharedSecret
		val hash = BigInteger.fromByteArray(digest.build(), Sign.POSITIVE).toString(16)

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
}