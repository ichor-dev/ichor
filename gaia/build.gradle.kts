@file:OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)

plugins {
  alias(jetbrains.plugins.mp)
  alias(ktorio.plugins.ktor)
  alias(jetbrains.plugins.serialization)
}

repositories {
  mavenCentral()
  maven("https://repo.nyon.dev/releases")
}

kotlin {
  explicitApi()

  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
  }
  linuxX64()

  sourceSets {
    all {
      languageSettings {
        optIn("fyi.pauli.ichor.gaia.extensions.internal.InternalGaiaApi")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(pauli.bundles.ichor)
        implementation(kotlinx.bundles.kotlinx)
        implementation(ktorio.bundles.ktor)
        implementation(klogging.bundles.logging)
        implementation(fileConfiguration.bundles.toml)
        implementation(koin.bundles.koin)
        implementation("dev.whyoleg.cryptography:cryptography-core:0.2.0")
        implementation("com.benasher44:uuid:0.8.2")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
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

tasks {
  test {
    useJUnitPlatform()
  }
}