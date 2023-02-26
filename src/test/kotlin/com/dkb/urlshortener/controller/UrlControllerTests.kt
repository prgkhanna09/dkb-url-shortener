package com.dkb.urlshortener.controller

import com.dkb.urlshortener.Constants
import com.dkb.urlshortener.handler.exception.InvalidURLException
import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.model.ShortenUrlResponse
import com.dkb.urlshortener.service.UrlService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.HttpClientErrorException.BadRequest
import reactor.core.publisher.Mono

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension::class)
class UrlControllerTests {

    @Mock
    private lateinit var urlService: UrlService

    @Test
    fun `testShortenUrlOK`() {
        val shortenUrlRequest = ShortenUrlRequest(Constants.GOOGLE_URL)
        val shortenUrlResponse = ShortenUrlResponse(Constants.SAMPLE_SHORT_URL)
        Mockito.`when`(urlService.shortenUrl(any())).thenReturn(Mono.just(Constants.SAMPLE_SHORT_URL))

        val webClient = WebTestClient.bindToController(UrlController(urlService)).build()
        webClient.post()
            .uri("/api/urls", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(shortenUrlRequest), ShortenUrlRequest::class.java)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ShortenUrlResponse::class.java)
            .isEqualTo(shortenUrlResponse)
    }

    @Test
    fun `testShortenUrlBadRequest`() {
        val originalURL = "sjanjeoqn"
        val shortenUrlRequest = ShortenUrlRequest(originalURL)
        Mockito.`when`(urlService.shortenUrl(any())).thenThrow(InvalidURLException())

        val webClient = WebTestClient.bindToController(UrlController(urlService)).build()
        webClient.post()
            .uri("/api/urls", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(shortenUrlRequest), ShortenUrlRequest::class.java)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(BadRequest::class.java)
    }

    @Test
    fun `testResolveShortUrlOK`() {
        Mockito.`when`(urlService.resolveShortUrl(ArgumentMatchers.anyString())).thenReturn(Mono.just(Constants.GOOGLE_URL))

        val webClient = WebTestClient.bindToController(UrlController(urlService)).build()
        webClient.get()
            .uri(Constants.SAMPLE_SHORT_URL_API)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo(Constants.GOOGLE_URL)
    }

    @Test
    fun `testResolveShortUrlBadRequest`() {
        Mockito.`when`(urlService.resolveShortUrl(ArgumentMatchers.anyString())).thenThrow(InvalidURLException())

        val webClient = WebTestClient.bindToController(UrlController(urlService)).build()
        webClient.get()
            .uri(Constants.SAMPLE_SHORT_URL_API)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(BadRequest::class.java)
    }
}
