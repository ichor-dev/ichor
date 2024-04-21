rootProject.name = "ichor"

include(
  ":gaia", ":hephaistos"
)

dependencyResolutionManagement {
  versionCatalogs {
    create("jetbrains") {
      version("kotlin", "1.9.23")

      plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef("kotlin")
      plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
    }

    create("ichor") {
      library("prorialize", "fyi.pauli.prorialize", "prorialize").version("1.1.2")
      library("nbterialize", "fyi.pauli.nbterialize", "nbterialize").version("1.0.2")

      bundle("ichor", listOf("prorialize", "nbterialize"))
    }

    create("kotlinx") {
      library("io", "org.jetbrains.kotlinx", "kotlinx-io-core").version("0.3.2")
      library("datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").version("0.5.0")
      library("json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.6.3")
      library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.8.0")

      bundle("kotlinx", listOf("coroutines", "coroutines", "datetime", "io"))
    }

    create("ktorio") {
      version("ktor", "2.3.10")
      library("cio", "io.ktor", "ktor-server-cio").versionRef("ktor")
      library("network", "io.ktor", "ktor-network").versionRef("ktor")
      library("core", "io.ktor", "ktor-server-core").versionRef("ktor")
      library("client", "io.ktor", "ktor-client-core").versionRef("ktor")
      library("client-cio", "io.ktor", "ktor-client-cio").versionRef("ktor")
      library("sockets", "io.ktor", "ktor-server-websockets").versionRef("ktor")
      library("serialization", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
      library("client-negotiation", "io.ktor", "ktor-client-content-negotiation").versionRef("ktor")

      bundle(
        "ktor",
        listOf("cio", "core", "network", "client", "client-cio", "sockets", "serialization", "client-negotiation")
      )
    }

    create("koin") {
      library("core", "io.insert-koin", "koin-core").version("3.5.6")
      library("coroutines", "io.insert-koin", "koin-core-coroutines").version("3.5.6")

      bundle("koin", listOf("core", "coroutines"))
    }

    create("klogging") {
      library("logging", "io.github.oshai", "kotlin-logging").version("6.0.9")

      bundle("logging", listOf("logging"))
    }

    create("fileConfiguration") {
      version("ktoml", "0.5.1")

      library("ktoml-core", "com.akuleshov7", "ktoml-core").versionRef("ktoml")

      bundle("toml", listOf("ktoml-core"))
    }

    create("cryptography"){
      library("core", "dev.whyoleg.cryptography", "cryptography-core").version("0.3.0")
    }

    create("kn") {
      library("uuid", "com.benasher44", "uuid").version("0.8.4")
    }

    create("testing") {
      library("jupiter", "org.junit.jupiter", "junit-jupiter-engine").version("5.10.2")
    }
  }
}