package fyi.pauli.ichor.gaia.extensions.internal

/**
 * @author btwonion
 * @since 20/11/2023
 */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
	level = RequiresOptIn.Level.ERROR,
	message = "This is an internal API and its use requires care. " +
		"It is subject to change or removal and is not intended for use outside the library." +
		"Make sure you fully read and understand documentation of the declaration that " +
		"is marked as an internal API."
)
public annotation class InternalGaiaApi