package fyi.pauli.ichor.gaia.server

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.providers.openssl3.Openssl3

/**
 * Cryptography provider for the linux platform.
 * @see CryptographyProvider.Companion.Openssl3
 */
public actual val Server.cryptographyProvider: CryptographyProvider
	get() = CryptographyProvider.Openssl3