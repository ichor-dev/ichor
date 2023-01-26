package dev.pooq.ichor.gaia.server

import dev.pooq.ichor.gaia.networking.client.Client
import io.ktor.network.sockets.*

abstract class Server {

  val clients: HashMap<ASocket, Client> = hashMapOf()

  abstract suspend fun startup(args: Array<String>)

  abstract suspend fun shutdown()

}