import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

plugins {
  alias(jetbrains.plugins.mp)
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
      kotlinOptions.jvmTarget = "17"
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
        api(project(":gaia"))
        api(kotlinx.bundles.kotlinx)
        api(ktorio.bundles.ktor)
        api(koin.bundles.koin)
        api(pauli.bundles.ichor)
        api(klogging.bundles.logging)
        implementation("dev.whyoleg.cryptography:cryptography-core:0.3.0-SNAPSHOT")
        api("com.benasher44:uuid:0.8.2")
        api("com.ionspin.kotlin:bignum:0.3.8")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(project(":gaia"))
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("dev.whyoleg.cryptography:cryptography-provider-jdk:0.2.0")
        implementation(kotlin("stdlib-jdk8"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
      }
    }

    val linuxX64Main by getting {
      dependencies {
        implementation("dev.whyoleg.cryptography:cryptography-provider-openssl3-prebuilt:0.3.0-SNAPSHOT")
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