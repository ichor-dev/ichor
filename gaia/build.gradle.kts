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
  api(database.bundles.exposed)
  api(database.bundles.drivers)
  api(koin.bundles.koin)
  api(klogging.bundles.logging)

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}