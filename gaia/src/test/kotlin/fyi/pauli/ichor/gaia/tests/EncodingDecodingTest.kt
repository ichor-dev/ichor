package fyi.pauli.ichor.gaia.tests

import fyi.pauli.ichor.gaia.models.Identifier
import fyi.pauli.ichor.gaia.networking.protocol.MinecraftProtocol
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.NumberType
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.StringLength
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.EnumSerial
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.objects.UuidByteSerializer
import fyi.pauli.ichor.gaia.networking.protocol.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author btwonion
 * @since 14/11/2023
 */
class EncodingDecodingTest {
	@Serializable
	data class NumberTest(
		val int: Int = 6,
		val biggerInt: Int = 786465849,
		@NumberType(MinecraftNumberType.VAR)
		val varInt: Int = 458,
		val long: Long = 5L,
		val biggerLong: Long = 78784535125L,
		@NumberType(MinecraftNumberType.VAR)
		val varLong: Long = 48974L,
		val short: Short = 5,
		val double: Double = 4.58
	)

	@Test
	fun `number tests`() {
		val mc = MinecraftProtocol()
		val test = NumberTest(double = 5.324)
		assertEquals(test, mc.toByteArrayAndBack(test))
	}

	@Serializable
	data class StringEnumTest(
		val string: String = "adwdasdasd awdad wdawd a",
		@StringLength(5)
		val sizedString: String = "sadad",
		@StringLength(5)
		val oversizedString: String = "asdawdwdad",
		val enum: TestEnum = TestEnum.Bar
	) {
		@Serializable
		enum class TestEnum {
			@EnumSerial(1) Foo, @EnumSerial(2) Bar
		}
	}

	@Test
	fun `string and enum test`() {
		val mc = MinecraftProtocol()
		val test = StringEnumTest(enum = StringEnumTest.TestEnum.Foo)
		assertEquals(test, mc.toByteArrayAndBack(test))
	}

	private inline fun <reified T> MinecraftProtocol.toByteArrayAndBack(value: T): T {
		val array = encodeToByteArray(value)
		return decodeFromByteArray(array)
	}

	@Serializable
	data class ObjectTest(
		@Serializable(with = UuidByteSerializer::class)
		val uuid: UUID,
		val identifier: Identifier = Identifier("ichor", "test"),
		val listTest: List<Identifier> = listOf(Identifier("ichor", "test"), Identifier("ichor", "test1"))
	)

	@Test
	fun `object test`() {
		val mc = MinecraftProtocol()
		val test = ObjectTest(uuid = UUID.fromString("404b7a45-be73-4665-b77e-07dfc6173eca"), identifier = Identifier("ichor", "asdawd-adw"))
		assertEquals(test, mc.toByteArrayAndBack(test))
	}
}