rootProject.name = "ichor"

include(
  ":gaia", ":hephaistos"
)

dependencyResolutionManagement {
  versionCatalogs {
    create("jetbrains") {
      version("kotlin", "1.9.10")

      plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
      plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
    }

    create("kotlinx") {
      library("datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").version("0.4.1")
      library("json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.6.0")
      library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.7.3")

      bundle("kotlinx", listOf("coroutines", "coroutines", "datetime"))
    }

    create("ktorio") {
      plugin("ktor", "io.ktor.plugin").version("2.3.4")

      library("cio", "io.ktor", "ktor-server-cio").withoutVersion()
      library("network", "io.ktor", "ktor-network").withoutVersion()
      library("core", "io.ktor", "ktor-server-core").withoutVersion()
      library("client", "io.ktor", "ktor-client-core").withoutVersion()
      library("client-cio", "io.ktor", "ktor-client-cio").withoutVersion()
      library("sockets", "io.ktor", "ktor-server-websockets").withoutVersion()
      library("serialization", "io.ktor", "ktor-serialization-kotlinx-json").withoutVersion()
      library("client-negotiation", "io.ktor", "ktor-client-content-negotiation").withoutVersion()

      bundle(
        "ktor",
        listOf("cio", "core", "network", "client", "client-cio", "sockets", "serialization", "client-negotiation")
      )
    }

    create("database") {
      version("exposed", "0.42.0")

      library("dao", "org.jetbrains.exposed", "exposed-dao").versionRef("exposed")
      library("core", "org.jetbrains.exposed", "exposed-core").versionRef("exposed")
      library("json", "org.jetbrains.exposed", "exposed-json").versionRef("exposed")
      library("jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("exposed")
      library("crypt", "org.jetbrains.exposed", "exposed-crypt").versionRef("exposed")
      library("datetime", "org.jetbrains.exposed", "exposed-kotlin-datetime").versionRef("exposed")

      bundle("exposed", listOf("dao", "core", "json", "jdbc", "crypt", "datetime"))

      library("h2", "com.h2database", "h2").version("2.2.224")
      library("mariadb", "org.mariadb.jdbc", "mariadb-java-client").version("2.2.0")
      library("mysql", "mysql", "mysql-connector-java").version("5.1.6")
      library("oracle", "com.oracle.database.jdbc", "ojdbc8").version("23.2.0.0")
      library("postgres", "org.postgresql", "postgresql").version("42.1.4")
      library("sql-server", "com.microsoft.sqlserver", "mssql-jdbc").version("6.1.0.jre7")
      library("sqlite", "org.xerial", "sqlite-jdbc").version("3.7.2")

      bundle("drivers", listOf("h2", "mariadb", "mysql", "oracle", "postgres", "sql-server", "sqlite"))
    }

    create("koin") {
      version("koin", "3.5.1")

      library("core", "io.insert-koin", "koin-core").versionRef("koin")
      library("ktor", "io.insert-koin", "koin-ktor").versionRef("koin")
      library("coroutines", "io.insert-koin", "koin-core-coroutines").versionRef("koin")

      bundle("koin", listOf("core", "ktor", "coroutines"))
    }

    create("klogging") {
      library("logback", "ch.qos.logback", "logback-classic").version("1.4.11")
      library("logging", "io.github.oshai", "kotlin-logging-jvm").version("5.1.0")

      bundle("logging", listOf("logback", "logging"))
    }

    create("fileConfiguration") {
      version("ktoml", "0.5.0")

      library("ktoml-core", "com.akuleshov7", "ktoml-core").versionRef("ktoml")
      library("ktoml-file", "com.akuleshov7", "ktoml-file").versionRef("ktoml")

      bundle("toml", listOf("ktoml-core", "ktoml-file"))
    }
  }
}