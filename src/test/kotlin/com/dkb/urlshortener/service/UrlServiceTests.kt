package com.dkb.urlshortener.service

import com.dkb.urlshortener.Constants
import com.dkb.urlshortener.cache.CacheManagerImpl
import com.dkb.urlshortener.entity.Url
import com.dkb.urlshortener.handler.exception.InvalidURLException
import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.repository.UrlRepository
import com.dkb.urlshortener.utils.RandomIdentifierGenerator
import com.dkb.urlshortener.utils.URLValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class UrlServiceTests {

    @Mock
    private lateinit var urlRepository: UrlRepository
    @Mock
    private lateinit var identifierGenerator: RandomIdentifierGenerator
    @Mock
    private lateinit var urlValidator: URLValidator
    @Mock
    private lateinit var cacheManager: CacheManagerImpl

    private lateinit var urlService: UrlService

    @BeforeEach
    fun init() {
        urlService = UrlService(identifierGenerator, urlValidator, urlRepository, cacheManager)
    }

    @Test
    fun shortenUrlValidationOK() {
        val urlRequest = ShortenUrlRequest(url = Constants.GOOGLE_URL)
        Mockito.`when`(urlValidator.validateURL(ArgumentMatchers.anyString())).thenReturn(true)
        Mockito.`when`(identifierGenerator.generateIdentifier()).thenReturn(Constants.SAMPLE_HASH)
        Mockito.`when`(urlRepository.save(any())).thenReturn(Mono.just(Url(originalUrl = Constants.SAMPLE_SHORT_URL, shortUrl = Constants.SAMPLE_HASH)))

        val originalUrlMono = urlService.shortenUrl(urlRequest)
        StepVerifier.create(originalUrlMono)
            .expectNext(Constants.SAMPLE_SHORT_URL)
            .expectComplete()
            .verify()
    }

    @Test
    fun shortenUrlValidationFailed() {
        val urlRequest = ShortenUrlRequest(url = Constants.GOOGLE_URL)
        Mockito.`when`(urlValidator.validateURL(ArgumentMatchers.anyString())).thenThrow(InvalidURLException())

        Assertions.assertThrows(InvalidURLException::class.java) {
            urlService.shortenUrl(urlRequest)
        }
    }

    @Test
    fun resolveShortUrlTest() {
        Mockito.`when`(urlValidator.validateURL(ArgumentMatchers.anyString())).thenReturn(true)
        Mockito.`when`(urlRepository.findByShortUrl(ArgumentMatchers.anyString())).thenReturn(Mono.just(Url(originalUrl = Constants.GOOGLE_URL, shortUrl = Constants.SAMPLE_SHORT_URL)))

        val originalUrlMono = urlService.resolveShortUrl(Constants.SAMPLE_SHORT_URL)
        StepVerifier.create(originalUrlMono)
            .expectNext(Constants.GOOGLE_URL)
            .expectComplete()
            .verify()
    }

    @Test
    fun resolveShortUrlTestValidationFailed() {
        Mockito.`when`(urlValidator.validateURL(ArgumentMatchers.anyString())).thenThrow(InvalidURLException())

        Assertions.assertThrows(InvalidURLException::class.java) {
            urlService.resolveShortUrl(Constants.SAMPLE_SHORT_URL)
        }
    }
}
