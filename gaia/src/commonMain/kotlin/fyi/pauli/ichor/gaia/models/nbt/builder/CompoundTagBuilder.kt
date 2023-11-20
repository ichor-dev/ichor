package fyi.pauli.ichor.gaia.models.nbt.builder

import fyi.pauli.ichor.gaia.models.nbt.Tag
import fyi.pauli.ichor.gaia.models.nbt.impl.*

/**
 * Build an NBT compound.
 */
public inline fun compoundTag(name: String?, build: CompoundTagBuilder.() -> Unit): CompoundTag =
	CompoundTagBuilder(name).apply(build).build()

/**
 * Build an NBT list.
 *
 * @see NbtListBuilder
 */
public inline fun listTag(name: String?, build: NbtListBuilder.() -> Unit): ListTag =
	NbtListBuilder(name).apply(build).build()

public class CompoundTagBuilder(name: String?) {
	private val compound = CompoundTag(name, mutableMapOf())

	public fun put(key: String, value: Tag<*>) {
		compound.value!![key] = value
	}

	public fun put(key: String, value: Boolean) {
		put(key, ByteTag(key, if (value) 1 else 0))
	}

	public fun put(key: String, value: Byte) {
		put(key, ByteTag(key, value))
	}

	public fun put(key: String, value: Short) {
		put(key, ShortTag(key, value))
	}

	public fun put(key: String, value: Int) {
		put(key, IntTag(key, value))
	}

	public fun put(key: String, value: Long) {
		put(key, LongTag(key, value))
	}

	public fun put(key: String, value: Float) {
		put(key, FloatTag(key, value))
	}

	public fun put(key: String, value: Double) {
		put(key, DoubleTag(key, value))
	}

	public fun put(key: String, value: Char) {
		put(key, IntTag(key, value.code))
	}

	public fun put(key: String, value: String) {
		put(key, StringTag(key, value))
	}

	public inline fun compound(key: String, build: CompoundTagBuilder.() -> Unit) {
		put(key, compoundTag(key, build))
	}

	public inline fun list(key: String, build: NbtListBuilder.() -> Unit) {
		put(key, listTag(key, build))
	}

	/**
	 * Puts an NBT list (*not* a primitive array) with all elements in `value`.
	 *
	 * @throws IllegalArgumentException if `T` not one of `Boolean`,
	 * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
	 */
	public inline fun <reified T> list(key: String, value: Iterable<T>) {
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

	public fun byteArray(key: String, value: ByteArray) {
		put(key, ByteArrayTag(key, value))
	}

	public fun byteArray(key: String, value: Collection<Byte>): Unit = byteArray(key, value.toByteArray())

	public fun intArray(key: String, value: IntArray) {
		put(key, IntArrayTag(key, value))
	}

	public fun intArray(key: String, value: Collection<Int>): Unit = intArray(key, value.toIntArray())

	public fun longArray(key: String, value: LongArray) {
		put(key, LongArrayTag(key, value))
	}

	public fun longArray(key: String, value: Collection<Long>): Unit = longArray(key, value.toLongArray())

	public fun build(): CompoundTag = compound
}

/**
 * Builder class for an NBT list.
 *
 * [ListTag] determines its type from the first element added, all following
 * elements are required to have the same type, otherwise an
 * [UnsupportedOperationException] is thrown.
 */
public class NbtListBuilder(name: String?) {
	private val list = ListTag(name, mutableListOf())

	public fun add(value: Tag<*>) {
		list.value!!.add(value)
	}

	public fun add(value: Boolean) {
		add(ByteTag(null, if (value) 1 else 0))
	}

	public fun add(value: Byte) {
		add(ByteTag(null, value))
	}

	public fun add(value: Short) {
		add(ShortTag(null, value))
	}

	public fun add(value: Int) {
		add(IntTag(null, value))
	}

	public fun add(value: Long) {
		add(LongTag(null, value))
	}

	public fun add(value: Float) {
		add(FloatTag(null, value))
	}

	public fun add(value: Double) {
		add(DoubleTag(null, value))
	}

	public fun add(value: Char) {
		add(IntTag(null, value.code))
	}

	public fun add(value: String) {
		add(StringTag(null, value))
	}

	public inline fun compound(build: CompoundTagBuilder.() -> Unit) {
		add(compoundTag(null, build))
	}

	public inline fun list(build: NbtListBuilder.() -> Unit) {
		add(listTag(null, build))
	}

	/**
	 * Adds an NBT list (*not* a primitive array) with all elements in `value`.
	 *
	 * @throws IllegalArgumentException if `T` not one of `Boolean`,
	 * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
	 */
	public inline fun <reified T> list(value: Iterable<T>) {
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

	public fun byteArray(vararg value: Byte) {
		add(ByteArrayTag(null, value))
	}

	public fun byteArray(value: Collection<Byte>): Unit = byteArray(*value.toByteArray())

	public fun intArray(vararg value: Int) {
		list.value!!.add(IntArrayTag(null, value))
	}

	public fun intArray(value: Collection<Int>): Unit = intArray(*value.toIntArray())

	public fun longArray(vararg value: Long) {
		list.value!!.add(LongArrayTag(null, value))
	}

	public fun longArray(value: Collection<Long>): Unit = longArray(*value.toLongArray())

	public fun build(): ListTag = list
}