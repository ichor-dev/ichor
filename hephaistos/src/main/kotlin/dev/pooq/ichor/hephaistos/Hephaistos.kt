package dev.pooq.ichor.hephaistos

import dev.pooq.ichor.gaia.extensions.client
import dev.pooq.ichor.gaia.extensions.varInt
import dev.pooq.ichor.gaia.networking.client.Client
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.networking.packet.server.status.StatusResponse
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class Hephaistos : Server(){

  override suspend fun startup(args: Array<String>) {
    val manager = SelectorManager(Dispatchers.Default)
    val serverSocket = aSocket(manager).tcp().bind("127.0.0.1", 25565)


    while (true){
      val socket = serverSocket.accept()

      val read = socket.openReadChannel()
      val write = socket.openWriteChannel(true)

      read.read { buffer ->
        println(buffer.varInt())

        val packet = ClientPackets.deserialize(buffer, clients.client(socket))

        if(packet.id == ClientPackets.STATUS_REQUEST.id){
          runBlocking(Dispatchers.Default) {
            write.writeAvailable(
              StatusResponse("{}").serialize()
            )
            write.close()
          }
        }
      }
    }
  }

  override suspend fun shutdown() {

  }
}

suspend fun main(args: Array<String>){
  val hephaistos = Hephaistos()

  Runtime.getRuntime().addShutdownHook(Thread{
    runBlocking {
      hephaistos.shutdown()
    }
  })

  hephaistos.startup(args)
}