package fyi.pauli.ichor.gaia.networking.serialization

import kotlinx.serialization.Serializable

/**
 * @author btwonion
 * @since 19/11/2023
 *
 * Serializable interface of a raw incoming packet.
 */
@Serializable
internal sealed interface RawPacket {
	/**
	 * The length of the data, which the server received.
	 */
	val length: Int

	/**
	 * A serializable class of a packet, which was recognized by its id.
	 */
	@Serializable
	data class Found(val id: Int, override val length: Int, val data: ByteArray) : RawPacket {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other == null || this::class != other::class) return false

			other as Found

			if (id != other.id) return false
			if (length != other.length) return false
			if (!data.contentEquals(other.data)) return false

			return true
		}

		override fun hashCode(): Int {
			var result = id
			result = 31 * result + length
			result = 31 * result + data.contentHashCode()
			return result
		}
	}

	/**
	 * A serializable class of a packet, which couldn't be recognized.
	 */
	@Serializable
	data class NotFound(override val length: Int, val data: ByteArray) : RawPacket {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other == null || this::class != other::class) return false

			other as NotFound

			if (length != other.length) return false
			if (!data.contentEquals(other.data)) return false

			return true
		}

		override fun hashCode(): Int {
			var result = length
			result = 31 * result + data.contentHashCode()
			return result
		}
	}
}