plugins {
  application
  kotlin("jvm") version "1.8.20"
  kotlin("plugin.serialization") version "1.8.20"
}

val ktorVersion: String by project
val serializationVersion: String by project
val ktomlVersion: String by project
val mordantVersion: String by project
val coroutinesVersion: String by project

repositories {
  mavenCentral()
}

dependencies {
  api("io.ktor", "ktor-server-core", ktorVersion)
  api("io.ktor", "ktor-server-cio", ktorVersion)
  api("io.ktor", "ktor-network", ktorVersion)
  api("io.ktor", "ktor-server-content-negotiation", ktorVersion)
  api("io.ktor", "ktor-serialization-kotlinx-json", ktorVersion)
  api("io.ktor", "ktor-client-core", ktorVersion)
  api("io.ktor", "ktor-client-cio", ktorVersion)
  api("io.ktor", "ktor-client-content-negotiation", ktorVersion)

  api("org.jetbrains.kotlinx", "kotlinx-serialization-json", serializationVersion)
  api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutinesVersion)

  api("com.akuleshov7", "ktoml-core-jvm", ktomlVersion)

  api("com.github.ajalt.mordant", "mordant", mordantVersion)
}

tasks.test {
  useJUnitPlatform()
}