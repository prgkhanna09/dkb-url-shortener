package com.dkb.urlshortener.controller

import com.dkb.urlshortener.model.ShortenUrlRequest
import com.dkb.urlshortener.model.ShortenUrlResponse
import com.dkb.urlshortener.service.UrlService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/urls")
@Validated
class UrlController(
    private val urlService: UrlService
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun shortenUrl(@RequestBody @Valid request: ShortenUrlRequest): Mono<ResponseEntity<ShortenUrlResponse>> {
        return urlService.shortenUrl(request).map { url -> ResponseEntity.ok(ShortenUrlResponse(url)) }
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun resolveShortUrl(@RequestParam("shortUrl") @Valid @NotBlank shortUrl: String): Mono<ResponseEntity<String>> {
        return urlService.resolveShortUrl(shortUrl).map { url -> ResponseEntity.ok(url) }
    }
}
