import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

plugins {
  alias(ichor.plugins.jetbrains.dokka)
  alias(ichor.plugins.jetbrains.multiplatform)
  alias(ichor.plugins.jetbrains.serialization)
  alias(ichor.plugins.github.release)
  `maven-publish`
  signing
}

repositories {
  mavenCentral()
  maven("https://repo.nyon.dev/releases")
}

kotlin {
  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }
  explicitApi()

  jvm {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
      jvmTarget = JvmTarget.JVM_1_8
    }
  }

  linuxX64()


  applyDefaultHierarchyTemplate()

  sourceSets {
    all {
      languageSettings.optIn("fyi.pauli.ichor.gaia.extensions.internal.InternalGaiaApi")
      languageSettings.enableLanguageFeature("InlineClasses")
    }

    val commonMain by getting {
      dependencies {
        api(ichor.uuid)
        api(ichor.ktoml)
        api(ichor.crypto.core)
        api(ichor.bignum)
        api(ichor.logging)
        api(ichor.bundles.koin)
        api(ichor.bundles.kotlinx)
        api(ichor.pauli.prorialize)
        api(ichor.pauli.nbterialize)
        api(ichor.bundles.ktor.client)
        api(ichor.bundles.ktor.server)
      }
    }

    val jvmMain by getting {
      dependencies {
        api(ichor.crypto.providers.jdk)
      }
    }

    val linuxMain by getting {
      dependencies {
        api(ichor.crypto.providers.openssl)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(ichor.testing.kotlin.common)
        implementation(ichor.testing.kotlin.annotations)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(ichor.testing.junit)
        implementation(ichor.testing.kotlin.junit)
      }
    }
  }
}

tasks.withType<KotlinJvmTest> {
  useJUnitPlatform()
}