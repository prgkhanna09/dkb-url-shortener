package com.dkb.urlshortener.utils

import com.dkb.urlshortener.handler.exception.InvalidURLException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class URLValidatorTests {

    @Test
    fun testURLValidTrue() {
        val url = "http://www.google.com/"
        val validator = URLValidator()
        assert(validator.validateURL(url))
    }

    @Test
    fun testURLValidFalse() {
        val url = "123456"
        val validator = URLValidator()
        assertThrows<InvalidURLException> {
            validator.validateURL(url)
        }
    }
}
