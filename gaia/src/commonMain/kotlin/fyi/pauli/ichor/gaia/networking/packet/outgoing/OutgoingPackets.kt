package fyi.pauli.ichor.gaia.networking.packet.outgoing

import fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.*
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.*
import fyi.pauli.ichor.gaia.networking.packet.outgoing.login.Disconnect
import fyi.pauli.ichor.gaia.networking.packet.outgoing.status.PingResponse
import fyi.pauli.ichor.gaia.networking.packet.outgoing.status.StatusResponse
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass

/**
 * @author btwonion
 * @since 03/12/2023
 */
public object OutgoingPackets {
	public val outgoingPacketSubClasses: MutableList<(PolymorphicModuleBuilder<OutgoingPacket>.() -> Unit)> =
		mutableListOf()

	public fun registerOutgoingPackets() {
		outgoingPacketSubClasses.add { subclass(StatusResponse::class) }
		outgoingPacketSubClasses.add { subclass(PingResponse::class) }

		outgoingPacketSubClasses.add { subclass(Disconnect::class) }
		outgoingPacketSubClasses.add { subclass(EncryptionRequest::class) }
		outgoingPacketSubClasses.add { subclass(LoginSuccess::class) }
		outgoingPacketSubClasses.add { subclass(SetCompression::class) }
		outgoingPacketSubClasses.add { subclass(LoginPluginRequest::class) }

		outgoingPacketSubClasses.add { subclass(PluginMessage::class) }
		outgoingPacketSubClasses.add { subclass(fyi.pauli.ichor.gaia.networking.packet.outgoing.configuration.Disconnect::class) }
		outgoingPacketSubClasses.add { subclass(KeepAlive::class) }
		outgoingPacketSubClasses.add { subclass(Ping::class) }
		outgoingPacketSubClasses.add { subclass(RegistryData::class) }
		outgoingPacketSubClasses.add { subclass(ResourcePack::class) }
		outgoingPacketSubClasses.add { subclass(FeatureFlags::class) }
		outgoingPacketSubClasses.add { subclass(UpdateTags::class) }

		// TODO: add play packets
	}
}