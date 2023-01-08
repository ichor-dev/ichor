plugins{
  application
  kotlin("jvm") version "1.8.0"
  kotlin("plugin.serialization") version "1.8.0"
}

val ktorVersion: String by project
val serializationVersion: String by project
val ktomlVersion: String by project

repositories{
  mavenCentral()
}

dependencies {
  api("io.ktor", "ktor-server-core", ktorVersion)
  api("io.ktor:ktor-server-netty:$ktorVersion")
  api("io.ktor:ktor-server-content-negotiation:$ktorVersion")
  api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
  api("io.ktor:ktor-serialization-kotlinx-protobuf:$ktorVersion")

  api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
  api("com.akuleshov7:ktoml-core-jvm:$ktomlVersion")
  api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serializationVersion")
}