package fyi.pauli.ichor.gaia.extensions.bytes.buffer

import fyi.pauli.ichor.gaia.entity.player.Property
import fyi.pauli.ichor.gaia.entity.player.UserProfile
import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.TagType
import fyi.pauli.ichor.gaia.models.nbt.impl.CompoundTag
import fyi.pauli.ichor.gaia.models.nbt.putTagString
import fyi.pauli.ichor.gaia.models.nbt.tagString
import java.nio.ByteBuffer
import java.util.*

fun ByteBuffer.compoundTag(tag: CompoundTag) {
	byte(tag.type.id.toByte())
	putTagString(tag.name ?: "")
	tag.write(this)
}

fun ByteBuffer.tag(): Tag<*> {
	val id = byte().toInt()
	val type = TagType.entries.first { it.id == id }
	val name = tagString()

	return type.read(this, name)
}

fun ByteBuffer.compoundTag(): CompoundTag {
	return tag() as CompoundTag
}

fun ByteBuffer.uuid(value: UUID) {
	long(value.mostSignificantBits)
	long(value.leastSignificantBits)
}

fun ByteBuffer.uuid(): UUID {
	return UUID(long(), long())
}

fun ByteBuffer.identifier(value: Identifier) {
	string(value.toString())
}

fun ByteBuffer.identifier(): Identifier {
	val split = string().split(':')
	val namespace = if (split.size > 1) split[0] else "minecraft"
	val value = if (split.size > 1) split[1] else split[0]

	return Identifier(namespace, value)
}

fun ByteBuffer.userProfile(value: UserProfile) {
	uuid(value.uuid)
	string(value.username)

	list(value.properties) {
		string(it.name)
		string(it.value)
		boolean(true)
		string(it.signature)
	}
	list(value.profileActions) {}
}

fun ByteBuffer.userProfile(): UserProfile {
	return UserProfile(uuid(), string(), list { Property(string(), string().also { boolean() }, string()) }, listOf())
}

inline fun <reified T> ByteBuffer.list(value: List<T>, crossinline processor: ByteBuffer.(T) -> Unit) {
	varInt(value.size)
	value.forEach { processor(this, it) }
}

inline fun <reified T> ByteBuffer.list(crossinline processor: ByteBuffer.() -> T): List<T> {
	val length = varInt()
	return List(length) { processor(this) }
}