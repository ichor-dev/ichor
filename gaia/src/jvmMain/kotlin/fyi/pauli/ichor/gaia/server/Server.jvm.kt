package fyi.pauli.ichor.gaia.server

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.providers.jdk.JDK

/**
 * Cryptography provider for the jvm platform.
 * @see CryptographyProvider.Companion.JDK
 */
public actual val Server.cryptographyProvider: CryptographyProvider
	get() = CryptographyProvider.JDK