import dev.pooq.ichor.gaia.extensions.bytes.byteSize
import dev.pooq.ichor.gaia.extensions.bytes.short
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.SHORT
import dev.pooq.ichor.gaia.networking.packet.State

suspend fun sendHandshake(state: State) {
	writeChannel.write {
		it.varInt(INT + INT + INT + "127.0.0.1".byteSize() + SHORT + INT)
		it.varInt(0x00)
		it.varInt(162)
		it.string("127.0.0.1")
		it.short(25565)
		it.varInt(
			when (state) {
				State.STATUS -> 1
				State.LOGIN -> 2
				else -> 0
			}
		)
	}
}

suspend fun sendStatusRequest() {
	writeChannel.write {
		it.varInt(INT)
		it.varInt(0x00)
	}
}