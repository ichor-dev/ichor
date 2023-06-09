plugins {
  application
  kotlin("jvm") version "1.8.20"
  kotlin("plugin.serialization") version "1.8.20"
}

repositories {
  mavenCentral()
}

dependencies {
  api(libs.ktor.server.core)
  api(libs.ktor.server.cio)
  api(libs.ktor.server.content.negotiation)

  api(libs.ktor.client.core)
  api(libs.ktor.client.cio)
  api(libs.ktor.client.content.negotiation)

  api(libs.ktor.serialization)

  api(libs.kotlinx.json)
  api(libs.kotlinx.coroutines)

  api(libs.ktoml)

  api(libs.mordant)

  api(libs.logback)

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}