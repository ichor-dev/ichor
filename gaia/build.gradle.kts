plugins {
  alias(jetbrains.plugins.jvm)
  alias(ktorio.plugins.ktor)
  alias(jetbrains.plugins.serialization)
}

repositories {
  mavenCentral()
}

dependencies {
  api(kotlinx.bundles.kotlinx)
  api(ktorio.bundles.ktor)
  api(klogging.bundles.logging)

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}