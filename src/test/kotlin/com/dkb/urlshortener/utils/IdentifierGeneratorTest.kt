package com.dkb.urlshortener.utils

import org.junit.jupiter.api.Test

class IdentifierGeneratorTest {

    @Test
    fun testGenerateIdentifier() {
        val identityGenerator = RandomIdentifierGenerator()
        val identifier = identityGenerator.generateIdentifier()
        assert(identifier.isNotEmpty())
    }
}
