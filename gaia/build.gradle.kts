@file:OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest


plugins {
  alias(jetbrains.plugins.mp)
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
        api(pauli.bundles.ichor)
        api(kotlinx.bundles.kotlinx)
        api(ktorio.bundles.ktor)
        api(klogging.bundles.logging)
        api(fileConfiguration.bundles.toml)
        api(koin.bundles.koin)
        api("dev.whyoleg.cryptography:cryptography-core:0.2.0")
        api("com.benasher44:uuid:0.8.2")
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

tasks.withType<KotlinJvmTest> {
  useJUnitPlatform()
}