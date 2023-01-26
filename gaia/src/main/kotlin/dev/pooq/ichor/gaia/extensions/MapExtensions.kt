package dev.pooq.ichor.gaia.extensions

import dev.pooq.ichor.gaia.networking.client.Client
import dev.pooq.ichor.gaia.networking.packet.State
import io.ktor.network.sockets.*

fun HashMap<ASocket, Client>.client(socket: Socket): Client{
  return getOrSetIfAbsent(socket, Client(State.STATUS))
}

fun <K, V> HashMap<K, V>.getOrSetIfAbsent(key: K, value: V) : V{
  val mapped = this[key]
  if(mapped == null){
    this[key] = value
    return value
  }

  return mapped
 }