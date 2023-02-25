package com.dkb.urlshortener.service

import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.utils.Constants
import com.dkb.urlshortener.utils.IdentifierGenerator
import com.dkb.urlshortener.utils.URLValidator
import org.springframework.stereotype.Service

@Service
class UrlService(
    private val identifierGenerator: IdentifierGenerator,
    private val urlValidator: URLValidator
) {

    fun createShortUrl(request: ShortenUrlRequest): String {
        urlValidator.validateURL(request.url)
        // We need to take care of concurrent requests here, there are possible solutions here:
        // 1. apply lock here, which will make request wait for the lock to be released
        // 2. We already have a unique constraints on shortUrl, as well as originalUrl + shortUrl combination. So duplicate entries will be avoided
        // 3. Using version in the Url, which serves as its optimistic lock value
        // These are the benefits we get by using RDBMS, if we decide to use NoSql in future we will have to check if the shortUrl for original Url exists or not
        // Since this is an MVP product and there are various ways to scale RDBMS as well, so using consistency as a benefit here compared to NoSql scaling for now
        val shortUrl = identifierGenerator.generateIdentifier()
        return Constants.DOMAIN + shortUrl
    }

    fun resolveShortUrl(shortUrl: String): String {
        urlValidator.validateURL(shortUrl)
        var hash = shortUrl.substringAfterLast("/")
        return "test$hash"
    }
}
