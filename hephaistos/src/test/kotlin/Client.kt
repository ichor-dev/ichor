import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers


lateinit var socket: Socket

lateinit var writeChannel: ByteWriteChannel
lateinit var readChannel: ByteReadChannel

suspend fun connect() {
	socket = aSocket(SelectorManager(Dispatchers.IO)).tcp().connect("127.0.0.1", 25565)
	readChannel = socket.openReadChannel()
	writeChannel = socket.openWriteChannel(true)
}
