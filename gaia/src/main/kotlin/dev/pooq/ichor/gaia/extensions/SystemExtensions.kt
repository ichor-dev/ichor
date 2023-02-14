package dev.pooq.ichor.gaia.extensions

fun env(key: String): String? = try {
  System.getenv(key)
} catch (e: NullPointerException) {
  null
}