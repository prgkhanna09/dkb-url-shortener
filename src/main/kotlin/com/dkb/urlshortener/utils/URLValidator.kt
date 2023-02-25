package com.dkb.urlshortener.utils

import org.springframework.stereotype.Component
import java.security.InvalidParameterException
import java.util.regex.Matcher
import java.util.regex.Pattern

@Component
class URLValidator {
    companion object {
        private const val URL_REGEX =
            "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"
        private val URL_PATTERN: Pattern = Pattern.compile(URL_REGEX)
    }

    fun validateURL(url: String?): Boolean {
        val m: Matcher = URL_PATTERN.matcher(url)
        if (!m.matches()) throw InvalidParameterException()
        return true
    }
}
