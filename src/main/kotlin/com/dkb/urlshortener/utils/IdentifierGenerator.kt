package com.dkb.urlshortener.utils

import org.springframework.stereotype.Component

// Creating the interface to create the abstraction,
// so in future if needed logic of generateIdentifier can be updated or implemented by the implementation class.
// Also, this part can be extracted out into a module or separate service or into a library.
interface IdentifierGenerator {
    fun generateIdentifier(): String
}

@Component
class RandomIdentifierGenerator : IdentifierGenerator {

    // not going deep the logic here but randomly generating 7 characters string.
    // On prod, we definitely need some external service to be called from here that gives us
    // the unique identifier, There are many solutions with different trade offs:
    // 1. use zookeeper to give us the range for each node
    // 2. Using Twitter Snowflake id in some manner although its 64 bits
    // 3. Base 62 encoding
    // 4. build in house unique counter generator and use it for this case
    override fun generateIdentifier(): String {
        // assuming the characters to be only alphanumeric and length to be 7
        val allowedChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val length = 7
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
