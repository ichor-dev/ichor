plugins {
  kotlin("jvm") version "1.8.10"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":gaia"))

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}