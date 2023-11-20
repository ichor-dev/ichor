plugins {
  application
  alias(jetbrains.plugins.mp)
  alias(jetbrains.plugins.serialization)
  alias(ktorio.plugins.ktor)
}

repositories {
  mavenCentral()
  maven("https://repo.nyon.dev/releases")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }

  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
  }
  linuxX64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":gaia"))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(project(":gaia"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
      }
    }
  }
}

application {
  mainClass.set("fyi.pauli.ichor.hephaistos.HephaistosKt")
}

tasks.test {
  useJUnitPlatform()
}