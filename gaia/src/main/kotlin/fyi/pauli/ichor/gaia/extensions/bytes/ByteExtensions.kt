package fyi.pauli.ichor.gaia.extensions.bytes

fun String.byteSize() = this.toByteArray(Charsets.UTF_8).size