package com.dkb.urlshortener

import com.dkb.urlshortener.utils.URLValidator
import org.junit.jupiter.api.Test

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
        assert(!validator.validateURL(url))
    }
}