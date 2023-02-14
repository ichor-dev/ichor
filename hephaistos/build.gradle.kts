plugins {
  kotlin("jvm") version "1.8.0"
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