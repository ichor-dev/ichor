package dev.pooq.ichor.hephaistos

import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.networking.packet.server.status.StatusResponse
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

suspend fun main(args: Array<String>){
  val manager = SelectorManager(Dispatchers.Default)
  val serverSocket = aSocket(manager).tcp().bind("127.0.0.1", 25565)

  while (true){
    val socket = serverSocket.accept()

    println("Accepted")

    val read = socket.openReadChannel()
    val write = socket.openWriteChannel(true)

    read.read { buffer ->

      val length = println(buffer.varInt())

      val packet = ClientPackets.deserialize(buffer)

      println("""
        Packet: ${packet.javaClass.simpleName}
        ID: ${packet.id}
        Length: $length
      """.trimIndent())

      if(packet.id == ClientPackets.STATUS_REQUEST.id){
        runBlocking(Dispatchers.Default) {
          write.writeAvailable(
            StatusResponse(exampleStatus).serialize()
          )
          write.close()
        }
      }
    }
  }
}

val exampleStatus = """
  {
    "version": {
        "name": "1.19.3",
        "protocol": 761
    },
    "players": {
        "max": 420,
        "online": 69,
        "sample": [
            {
                "name": "thinkofdeath",
                "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
            }
        ]
    },
    "description": {
        "text": "§rHello world"
    },
    "favicon": "data:image/png;base64,<data>",
    "previewsChat": false,
    "enforcesSecureChat": false,
}
""".trimIndent()