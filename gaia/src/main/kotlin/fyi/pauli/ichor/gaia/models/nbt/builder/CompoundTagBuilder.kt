package fyi.pauli.ichor.gaia.models.nbt.builder

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.impl.*

/**
 * Build an NBT compound.
 */
inline fun compoundTag(name: String?, build: CompoundTagBuilder.() -> Unit) =
	CompoundTagBuilder(name).apply(build).build()

/**
 * Build an NBT list.
 *
 * @see NbtListBuilder
 */
inline fun listTag(name: String?, build: NbtListBuilder.() -> Unit) = NbtListBuilder(name).apply(build).build()

class CompoundTagBuilder(name: String?) {
	private val compound = CompoundTag(name, mutableMapOf())

	fun put(key: String, value: Tag<*>) {
		compound.value!![key] = value
	}

	fun put(key: String, value: Boolean) {
		put(key, ByteTag(key, if (value) 1 else 0))
	}

	fun put(key: String, value: Byte) {
		put(key, ByteTag(key, value))
	}

	fun put(key: String, value: Short) {
		put(key, ShortTag(key, value))
	}

	fun put(key: String, value: Int) {
		put(key, IntTag(key, value))
	}

	fun put(key: String, value: Long) {
		put(key, LongTag(key, value))
	}

	fun put(key: String, value: Float) {
		put(key, FloatTag(key, value))
	}

	fun put(key: String, value: Double) {
		put(key, DoubleTag(key, value))
	}

	fun put(key: String, value: Char) {
		put(key, IntTag(key, value.code))
	}

	fun put(key: String, value: String) {
		put(key, StringTag(key, value))
	}

	inline fun compound(key: String, build: CompoundTagBuilder.() -> Unit) {
		put(key, compoundTag(key, build))
	}

	inline fun list(key: String, build: NbtListBuilder.() -> Unit) {
		put(key, listTag(key, build))
	}

	/**
	 * Puts an NBT list (*not* a primitive array) with all elements in `value`.
	 *
	 * @throws IllegalArgumentException if `T` not one of `Boolean`,
	 * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
	 */
	inline fun <reified T> list(key: String, value: Iterable<T>) {
		when (T::class) {
			Boolean::class -> list(key) { value.forEach { add(it as Boolean) } }
			Byte::class -> list(key) { value.forEach { add(it as Byte) } }
			Short::class -> list(key) { value.forEach { add(it as Short) } }
			Int::class -> list(key) { value.forEach { add(it as Int) } }
			Long::class -> list(key) { value.forEach { add(it as Long) } }
			Float::class -> list(key) { value.forEach { add(it as Float) } }
			Double::class -> list(key) { value.forEach { add(it as Double) } }
			Char::class -> list(key) { value.forEach { add(it as Char) } }
			String::class -> list(key) { value.forEach { add(it as String) } }
			else -> throw IllegalArgumentException("Type ${T::class} is not a valid NBT type")
		}
	}

	fun byteArray(key: String, value: ByteArray) {
		put(key, ByteArrayTag(key, value))
	}

	fun byteArray(key: String, value: Collection<Byte>) = byteArray(key, value.toByteArray())

	fun intArray(key: String, value: IntArray) {
		put(key, IntArrayTag(key, value))
	}

	fun intArray(key: String, value: Collection<Int>) = intArray(key, value.toIntArray())

	fun longArray(key: String, value: LongArray) {
		put(key, LongArrayTag(key, value))
	}

	fun longArray(key: String, value: Collection<Long>) = longArray(key, value.toLongArray())

	fun build() = compound
}

/**
 * Builder class for an NBT list.
 *
 * [ListTag] determines its type from the first element added, all following
 * elements are required to have the same type, otherwise an
 * [UnsupportedOperationException] is thrown.
 */
class NbtListBuilder(name: String?) {
	val list = ListTag(name, mutableListOf())

	fun add(value: Tag<*>) {
		list.value!!.add(value)
	}

	fun add(value: Boolean) {
		list.value!!.add(ByteTag(null, if (value) 1 else 0))
	}

	fun add(value: Byte) {
		list.value!!.add(ByteTag(null, value))
	}

	fun add(value: Short) {
		list.value!!.add(ShortTag(null, value))
	}

	fun add(value: Int) {
		list.value!!.add(IntTag(null, value))
	}

	fun add(value: Long) {
		list.value!!.add(LongTag(null, value))
	}

	fun add(value: Float) {
		list.value!!.add(FloatTag(null, value))
	}

	fun add(value: Double) {
		list.value!!.add(DoubleTag(null, value))
	}

	fun add(value: Char) {
		list.value!!.add(IntTag(null, value.code))
	}

	fun add(value: String) {
		list.value!!.add(StringTag(null, value))
	}

	inline fun compound(build: CompoundTagBuilder.() -> Unit) {
		list.value!!.add(compoundTag(null, build))
	}

	inline fun list(build: NbtListBuilder.() -> Unit) {
		list.value!!.add(listTag(null, build))
	}

	/**
	 * Adds an NBT list (*not* a primitive array) with all elements in `value`.
	 *
	 * @throws IllegalArgumentException if `T` not one of `Boolean`,
	 * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
	 */
	inline fun <reified T> list(value: Iterable<T>) {
		when (T::class) {
			Boolean::class -> list { value.forEach { add(it as Boolean) } }
			Byte::class -> list { value.forEach { add(it as Byte) } }
			Short::class -> list { value.forEach { add(it as Short) } }
			Int::class -> list { value.forEach { add(it as Int) } }
			Long::class -> list { value.forEach { add(it as Long) } }
			Float::class -> list { value.forEach { add(it as Float) } }
			Double::class -> list { value.forEach { add(it as Double) } }
			Char::class -> list { value.forEach { add(it as Char) } }
			String::class -> list { value.forEach { add(it as String) } }
			else -> throw IllegalArgumentException("Type ${T::class} is not a valid NBT type")
		}
	}

	fun byteArray(vararg value: Byte) {
		list.value!!.add(ByteArrayTag(null, value))
	}

	fun byteArray(value: Collection<Byte>) = byteArray(*value.toByteArray())

	fun intArray(vararg value: Int) {
		list.value!!.add(IntArrayTag(null, value))
	}

	fun intArray(value: Collection<Int>) = intArray(*value.toIntArray())

	fun longArray(vararg value: Long) {
		list.value!!.add(LongArrayTag(null, value))
	}

	fun longArray(value: Collection<Long>) = longArray(*value.toLongArray())

	fun build() = list
}