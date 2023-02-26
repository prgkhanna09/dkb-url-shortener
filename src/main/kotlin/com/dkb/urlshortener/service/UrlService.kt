package com.dkb.urlshortener.service

import com.dkb.urlshortener.entity.Url
import com.dkb.urlshortener.handler.exception.UrlPersistException
import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.repository.UrlRepository
import com.dkb.urlshortener.utils.Constants
import com.dkb.urlshortener.utils.IdentifierGenerator
import com.dkb.urlshortener.utils.URLValidator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class UrlService(
    private val identifierGenerator: IdentifierGenerator,
    private val urlValidator: URLValidator,
    private val urlRepository: UrlRepository
) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java.toString())!!
    }

    @Throws(UrlPersistException::class)
    @Transactional
    fun shortenUrl(request: ShortenUrlRequest): Mono<String> {
        val originalUrl = request.url
        urlValidator.validateURL(originalUrl)

        log.info("Generating short URL for : {}", originalUrl)
        // We need to take care of concurrent requests here, there are possible solutions here:
        // 1. apply lock here, which will make request wait for the lock to be released
        // 2. We already have a unique constraints on shortUrl, as well as originalUrl + shortUrl combination. So duplicate entries will be avoided
        // 3. Using version in the Url, which serves as its optimistic lock value
        // These are the benefits we get by using RDBMS, if we decide to use NoSql in future we will have to check if the shortUrl for original Url exists or not
        // Since this is an MVP product and there are various ways to scale RDBMS as well, so using consistency as a benefit here compared to NoSql scaling for now
        val shortUrl = identifierGenerator.generateIdentifier()
        val url = Url(originalUrl = originalUrl, shortUrl = shortUrl)
        return urlRepository.save(url).map { savedUrl -> Constants.DOMAIN + savedUrl!!.shortUrl }
    }

    fun resolveShortUrl(shortUrl: String): Mono<String> {
        urlValidator.validateURL(shortUrl)
        log.info("Resolving URL for : {}", shortUrl)
        var hash = shortUrl.substringAfterLast("/").trim()
        return urlRepository.findByShortUrl(hash)
            .map { url ->
                {
                    url.originalUrl
                }
            }
            .map { it.invoke() }
    }
}
