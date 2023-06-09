rootProject.name = "ichor"

include("gaia")
include("hephaistos")
include("hermes")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {

      version("ktor", "2.2.4")
      version("kotlinx", "1.5.0")
      version("ktoml", "0.4.1")
      version("mordant", "2.0.0-beta12")
      version("coroutines", "1.6.4")
      version("hoplite", "2.7.2")
      version("logback", "1.4.7")

      //ktor

      val ktor = "ktor"
      val ktorGroup = "io.$ktor"
      val ktorServer = "$ktor-server"

      //server

      library("$ktorServer-core", ktorGroup, "$ktorServer-core").versionRef(ktor)
      library("$ktorServer-cio", ktorGroup, "$ktorServer-cio").versionRef(ktor)
      library("$ktorServer-content-negotiation", ktorGroup, "$ktorServer-content-negotiation").versionRef(ktor)

      //client

      val ktorClient = "$ktor-client"

      library("$ktorClient-core", ktorGroup, "$ktorClient-core").versionRef(ktor)
      library("$ktorClient-cio", ktorGroup, "$ktorClient-cio").versionRef(ktor)
      library("$ktorClient-content-negotiation", ktorGroup, "$ktorClient-content-negotiation").versionRef(ktor)

      //network

      library("$ktor-network", ktorGroup, "$ktor-network").versionRef(ktor)

      //serialization

      library("$ktor-serialization", ktorGroup, "$ktor-serialization-kotlinx-json").versionRef(ktor)

      val kotlinx = "kotlinx"
      val kotlinxGroup = "org.jetbrains.$kotlinx"

      //kotlinx

      library("$kotlinx-json", kotlinxGroup, "$kotlinx-serialization-json").versionRef(kotlinx)
      library("$kotlinx-coroutines", kotlinxGroup, "$kotlinx-coroutines-core").versionRef(kotlinx)

      //ktoml

      library("ktoml", "com.akuleshov7", "ktoml-core-jvm").versionRef("ktoml")

      //mordant

      library("mordant", "com.github.ajalt.mordant", "mordant").versionRef("mordant")

      //logback

      library("logback", "ch.qos.logback", "logback-classic").versionRef("logback")
    }
  }
}