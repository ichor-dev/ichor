plugins {
  application
  alias(jetbrains.plugins.jvm)
  alias(jetbrains.plugins.serialization)
  alias(ktorio.plugins.ktor)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":gaia"))

  testImplementation(kotlin("test"))
  testImplementation(project(":gaia"))
}

application {
  mainClass.set("fyi.pauli.ichor.hephaistos.HephaistosKt")
}

tasks.test {
  useJUnitPlatform()
}