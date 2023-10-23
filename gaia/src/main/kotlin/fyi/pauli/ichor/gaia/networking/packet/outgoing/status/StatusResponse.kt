package fyi.pauli.ichor.gaia.networking.packet.outgoing.status

import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.packet
import fyi.pauli.ichor.gaia.extensions.bytes.buffer.string
import fyi.pauli.ichor.gaia.networking.packet.State
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket

/**
 * The response packet for StatusRequest.
 *
 * @param status The status response
 */
data class StatusResponse(
	var status: String
) : OutgoingPacket() {

	override fun serialize(): RawPacket {
		return packet {
			string("""
				{
				    "version": {
				        "name": "1.19.4",
				        "protocol": 762
				    },
				    "players": {
				        "max": 100,
				        "online": 5,
				        "sample": [
				            {
				                "name": "thinkofdeath",
				                "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
				            }
				        ]
				    },
				    "description": {
				        "text": "Hello world"
				    },
				    "favicon": "data:image/png;base64,<data>",
				    "enforcesSecureChat": true,
				    "previewsChat": true
				}
			""".trimIndent())
		}
	}

	override val id: Int
		get() = 0x00

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Status Response"
}
