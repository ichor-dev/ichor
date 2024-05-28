import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

plugins {
  alias(ichor.plugins.jetbrains.multiplatform)
  alias(ichor.plugins.jetbrains.serialization)
}

version = "1.0.0"

repositories {
  mavenCentral()
  maven("https://repo.nyon.dev/releases")
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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

  jvmToolchain(17)

  jvm {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }

  linuxX64()

  //linuxArm64() Unsupported due to the not existing linuxArm support of ktoml.

  /**
   * macosX64()
   * macosArm64()
   *
   * Cant support darwin targets due to missing hardware.
   */

  applyDefaultHierarchyTemplate()

  sourceSets {
    all {
      languageSettings.optIn("fyi.pauli.ichor.gaia.extensions.internal.InternalGaiaApi")
      languageSettings.enableLanguageFeature("InlineClasses")
    }

    val commonMain by getting {
      dependencies {
        implementation(project(":gaia"))
        implementation(ichor.logging)
        implementation(ichor.ktor.client.cio)
        implementation(ichor.ktor.client.negotiation)
      }
    }
  }
}

tasks {
  withType<KotlinJvmTest> {
    useJUnitPlatform()
  }

  withType(Jar::class.java) {
    manifest {
      attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
      attributes["Implementation-Title"] = project.name
      attributes["Implementation-Version"] = project.version.toString()
      attributes["Main-Class"] = "fyi.pauli.ichor.hephaistos.HephaistosKt"
    }
  }
}