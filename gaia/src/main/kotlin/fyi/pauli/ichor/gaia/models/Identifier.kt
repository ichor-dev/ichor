package fyi.pauli.ichor.gaia.models

data class Identifier(val namespace: String, val value: String) {
	override fun toString(): String {
		return "$namespace:$value"
	}
}