plugins {
  kotlin("jvm") version "1.8.20"
}

repositories {
  mavenCentral()
}

val ktorVersion: String by project

dependencies {
  implementation(project(":gaia"))

  testImplementation(kotlin("test"))
  testImplementation(project(":gaia"))
  testImplementation("io.ktor:ktor-network:$ktorVersion")
}

tasks.test {
  useJUnitPlatform()
}