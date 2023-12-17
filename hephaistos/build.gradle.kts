import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

plugins {
  alias(jetbrains.plugins.multiplatform)
  alias(jetbrains.plugins.serialization)
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
        jvmTarget = "17"
      }
    }

    mainRun {
      mainClass = "fyi.pauli.ichor.hephaistos.HephaistosKt"
    }
  }

  linuxX64 {
    binaries {
      executable {
        entryPoint = "fyi.pauli.ichor.hephaistos.main"
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":gaia"))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(project(":gaia"))
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(cryptography.core)
        implementation(kotlin("stdlib-jdk8"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(testing.jupiter)
        implementation(kotlin("test-junit5"))
      }
    }

    val linuxX64Main by getting {
      dependencies {
        implementation(cryptography.core)
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