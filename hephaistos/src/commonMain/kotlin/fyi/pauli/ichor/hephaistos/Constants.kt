package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.networking.serialization.UuidStringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

public object Constants {
	public const val SERVER_BRAND: String = "hephaistos"

	public val json: Json = Json {
		encodeDefaults = true

		serializersModule = SerializersModule {
			contextual(UuidStringSerializer)
		}
	}
}