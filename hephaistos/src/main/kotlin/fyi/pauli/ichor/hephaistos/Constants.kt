package fyi.pauli.ichor.hephaistos

import fyi.pauli.ichor.gaia.extensions.serializers.UUIDSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object Constants {
	const val serverBrand = "hephaistos"

	val json: Json = Json {
		encodeDefaults = true

		serializersModule = SerializersModule {
			contextual(UUIDSerializer)
		}
	}
}