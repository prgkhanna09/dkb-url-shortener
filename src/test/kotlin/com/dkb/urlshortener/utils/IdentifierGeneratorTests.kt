package com.dkb.urlshortener.utils

import org.junit.jupiter.api.Test

class IdentifierGeneratorTests {

    @Test
    fun testGenerateIdentifier() {
        val identityGenerator = RandomIdentifierGenerator()
        val identifier = identityGenerator.generateIdentifier()
        assert(identifier.isNotEmpty())
    }
}
