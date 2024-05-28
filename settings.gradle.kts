rootProject.name = "ichor"

include(
  ":gaia", ":hephaistos"
)

dependencyResolutionManagement {
  repositories {
    maven("https://repo.nyon.dev/releases")
  }
  versionCatalogs {
    create("ichor") {
      from("fyi.pauli:ichor-catalog:1.6.3")
    }
  }
}