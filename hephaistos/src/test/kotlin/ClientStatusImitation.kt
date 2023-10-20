import fyi.pauli.ichor.gaia.extensions.bytes.RawPacket
import fyi.pauli.ichor.gaia.extensions.bytes.build
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import java.nio.ByteBuffer
import kotlin.experimental.and

suspend fun main() {
	val selectorManager = SelectorManager(Dispatchers.IO)
	val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 25565)

	val readChannel = socket.openReadChannel()
	val writeChannel = socket.openWriteChannel(true)
	writeChannel.awaitFreeSpace()
	writeChannel.writeFully(RawPacket(0x00, ByteBuffer.allocate(0)).build())

	while (!readChannel.isClosedForRead) {
		val size = run {
			var i = 0
			var j = 0

			var b: Byte
			do {
				b = readChannel.readByte()
				i = i or (b.toInt() and 127 shl j++ * 7)
				if (j > 5) {
					throw RuntimeException("VarInt too big")
				}
			} while ((b and 128.toByte()).toInt() == 128)

			return@run i
		}

		val buffer = ByteBuffer.allocate(size)
		readChannel.readAvailable(buffer)
		buffer.flip()

		println(buffer.array().joinToString { it.toString() })
	}
}