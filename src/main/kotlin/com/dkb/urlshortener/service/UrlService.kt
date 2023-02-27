package com.dkb.urlshortener.service

import com.dkb.urlshortener.cache.CacheManagerImpl
import com.dkb.urlshortener.entity.Url
import com.dkb.urlshortener.handler.exception.UrlPersistException
import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.model.UrlCache
import com.dkb.urlshortener.repository.UrlRepository
import com.dkb.urlshortener.utils.Constants
import com.dkb.urlshortener.utils.IdentifierGenerator
import com.dkb.urlshortener.utils.URLValidator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.Objects

@Service
class UrlService(
    private val identifierGenerator: IdentifierGenerator,
    private val urlValidator: URLValidator,
    private val urlRepository: UrlRepository,
    private val cacheManager: CacheManagerImpl
    // Can also add datadog metrics for monitoring, can also add actuator for health monitoring for the service
) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java.toString())!!
    }

    @Throws(UrlPersistException::class)
    @Transactional
    // Use WriteTransaction here to read from primary dataSource
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
        val cachedUrl: UrlCache? = cacheManager.get(cacheManager.getUrlKey(originalUrl)) as? UrlCache
        if (Objects.nonNull(cachedUrl)) return Mono.just(Constants.DOMAIN + cachedUrl!!.shortUrl)

        val shortUrl = identifierGenerator.generateIdentifier()
        val url = Url(originalUrl = originalUrl, shortUrl = shortUrl)
        // TODO: Write only to primary replica using WriteTransaction & update the cache
        return urlRepository.save(url).map { savedUrl ->
            val urlCache = UrlCache(savedUrl.originalUrl, savedUrl.shortUrl)
            cacheManager.save(cacheManager.getUrlKey(savedUrl.shortUrl), urlCache)
            cacheManager.save(cacheManager.getUrlKey(originalUrl), urlCache)
            Constants.DOMAIN + savedUrl!!.shortUrl
        }
    }

    // Use ReadTransaction here to read from secondary dataSource
    fun resolveShortUrl(shortUrl: String): Mono<String> {
        urlValidator.validateURL(shortUrl)
        log.info("Resolving URL for : {}", shortUrl)
        var hash = shortUrl.substringAfterLast("/").trim()
        var cachedUrl: UrlCache? = cacheManager.get(cacheManager.getUrlKey(hash)) as? UrlCache
        if (Objects.nonNull(cachedUrl)) return Mono.just(cachedUrl!!.originalUrl)
        // TODO: Read from the caching layer, also read from read replica using ReadTransaction
        return urlRepository.findByShortUrl(hash)
            .map { url -> url.originalUrl }
    }
}
