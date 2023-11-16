package fyi.pauli.ichor.gaia.networking.protocol.exceptions

/**
 * @author btwonion
 * @since 11/11/2023
 * 
 * Exception which will be thrown when an error occurs during decoding.
 */
class MinecraftProtocolDecodingException(message: String) : RuntimeException(message)