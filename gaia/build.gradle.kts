@file:OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest


plugins {
  alias(jetbrains.plugins.multiplatform)
  alias(jetbrains.plugins.serialization)
}

repositories {
  mavenCentral()
  maven("https://repo.nyon.dev/releases")
}

kotlin {
  targets.configureEach {
    compilations.configureEach {
      compilerOptions.configure {
        freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }
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
        api(kn.uuid)
        api(koin.bundles.koin)
        api(cryptography.core)
        api(ichor.bundles.ichor)
        api(ktorio.bundles.ktor)
        api(kotlinx.bundles.kotlinx)
        api(klogging.bundles.logging)
        api(fileConfiguration.bundles.toml)
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
        implementation(testing.jupiter)
        implementation(kotlin("test-junit5"))
      }
    }
  }
}

tasks.withType<KotlinJvmTest> {
  useJUnitPlatform()
}