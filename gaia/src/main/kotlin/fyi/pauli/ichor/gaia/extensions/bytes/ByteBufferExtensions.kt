package fyi.pauli.ichor.gaia.extensions.bytes

import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.impl.CompoundTag
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import fyi.pauli.ichor.gaia.networking.INT
import fyi.pauli.ichor.gaia.networking.packet.outgoing.OutgoingPacket
import java.nio.ByteBuffer
import java.util.*


private const val SEGMENT_BITS = 0x7F
private const val SEGMENT_BITS_LONG = (0x7F).toLong()
private const val CONTINUE_BIT = 0x80
private const val CONTINUE_BIT_LONG = (0x80).toLong()

inline fun OutgoingPacket.uncompressedBuffer(applier: ByteBuffer.() -> Unit = {}): ByteBuffer {
	val data = ByteBuffer.allocate(1024).apply { varInt(id) }.apply(applier)
	val dataSize = data.position()

	return ByteBuffer.allocate(INT + dataSize).apply {
		varInt(dataSize)
		put(data.array(), 0, dataSize)
	}
}

inline fun OutgoingPacket.compressedBuffer(applier: ByteBuffer.() -> Unit = {}): ByteBuffer {
	val uncompressed = ByteBuffer.allocate(1024).apply { varInt(id) }.apply(applier)
	val uncompressedDataSize = uncompressed.position()

	val compressed = ByteBuffer.wrap(uncompressed.array().compress())
	val compressedDataSize = compressed.remaining()

	return ByteBuffer.allocate(INT + INT + compressedDataSize).apply {
		varInt(uncompressedDataSize)
		varInt(compressedDataSize)
		put(compressed.array(), 0, compressedDataSize)
	}
}


fun ByteBuffer.varInt(): Int {
	var value = 0
	var position = 0
	var currentByte: Byte

	while (true) {
		currentByte = get()
		value = value or (currentByte.toInt() and SEGMENT_BITS shl position)
		if (currentByte.toInt() and CONTINUE_BIT == 0) break
		position += 7
		if (position >= 32) throw RuntimeException("VarInt is too big")
	}

	return value
}

fun ByteBuffer.varLong(): Long {
	var value: Long = 0
	var position = 0
	var currentByte: Byte

	while (true) {
		currentByte = get()
		value = value or ((currentByte.toInt() and SEGMENT_BITS).toLong() shl position)
		if (currentByte.toInt() and CONTINUE_BIT == 0) break
		position += 7
		if (position >= 64) throw java.lang.RuntimeException("VarLong is too big")
	}

	return value
}

fun ByteBuffer.boolean(): Boolean {
	return get().toInt() == 0x01
}

fun ByteBuffer.boolean(boolean: Boolean) {
	put(if (boolean) 1 else 0)
}

fun ByteBuffer.string(): String {
	val maxLength = Short.MAX_VALUE
	val length = varInt()

	require(length <= (maxLength * 4) + 3) { "String is too long" }

	if (hasArray()) return String(array())

	val bytes = ByteArray(remaining())
	get(bytes)
	return String(ByteArray(remaining()))
}

fun ByteBuffer.short(): Short {
	return (get().toInt() and 0xFF shl 8 or (get().toInt() and 0xFF)).toShort()
}

fun ByteBuffer.uuid(): UUID {
	return UUID(varLong(), varLong())
}

fun ByteBuffer.uuid(uuid: UUID) {
	varLong(uuid.mostSignificantBits)
	varLong(uuid.leastSignificantBits)
}

fun ByteBuffer.varLong(value: Long) {
	var remainingValue = value
	while (true) {
		if ((remainingValue and SEGMENT_BITS_LONG.inv()) == 0L) {
			this.put(remainingValue.toByte())
			return
		}

		val byteToWrite = (remainingValue and SEGMENT_BITS_LONG) or CONTINUE_BIT_LONG
		this.put(byteToWrite.toByte())

		remainingValue = remainingValue ushr 7
	}
}

fun ByteBuffer.varInt(value: Int) {
	var remainingValue = value
	while (true) {
		if ((remainingValue and SEGMENT_BITS.inv()) == 0) {
			this.put(remainingValue.toByte())
			return
		}

		val byteToWrite = (remainingValue and SEGMENT_BITS) or CONTINUE_BIT
		this.put(byteToWrite.toByte())

		remainingValue = remainingValue ushr 7
	}
}

fun ByteBuffer.string(string: String) {
	val maxLength = Short.MAX_VALUE

	require(string.length <= maxLength) { "String is too long." }

	val bytes = string.toByteArray(Charsets.UTF_8)

	if (bytes.size > (maxLength * 4) + 3) throw throw IllegalArgumentException("String bytearray is too long.")

	put(bytes)
}

fun ByteBuffer.short(short: Short) {
	this.put((short.toInt() ushr 8 and 0xFF).toByte())
	this.put((short.toInt() and 0xFF).toByte())
}

fun ByteBuffer.byteArray(length: Int) = ByteArray(length).also(this::get)

fun ByteBuffer.identifier(): Identifier {
	val split = string().split(':')
	val namespace = if (split.size > 1) split[0] else "minecraft"
	val value = if (split.size > 1) split[1] else split[0]

	return Identifier(namespace, value)
}

fun ByteBuffer.identifier(identifier: Identifier) {
	string(identifier.toString())
}

fun ByteBuffer.compoundTag(tag: CompoundTag) {
	put(TagType.COMPOUND.id.toByte())
	putInt(0)
	putTagString("")
	tag.write(this)
}

fun ByteBuffer.tag(): Tag<*> {
	val type = TagType.entries.first { it.id == get().toInt() }
	val name = tagString

	return type.read(this, name)
}

fun ByteBuffer.compoundTag(): CompoundTag {
	return tag() as CompoundTag
}

fun ByteBuffer.byteArray(array: ByteArray) {
	varInt(array.size)
	put(array)
}

fun ByteBuffer.userProfile(profile: UserProfile) {
	uuid(profile.uuid)
	string(profile.username)

	varInt(profile.properties.size)
	profile.properties.forEach {
		string(it.name)
		string(it.value)
		boolean(true)
		string(it.signature)
	}
}

fun ByteBuffer.userProfile(): UserProfile {
	return UserProfile(uuid(), string(), List(varInt()) { Property(string(), string().also { boolean() }, string()) })
}