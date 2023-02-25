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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/urls")
@Validated
class UrlController(
    private val urlService: UrlService
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun shortenUrl(@RequestBody @Valid request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {
        val shortUrl = urlService.createShortUrl(request)
        return ResponseEntity.ok(ShortenUrlResponse(shortUrl))
    }

    @GetMapping("/{shortUrl}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getFullUrl(@PathVariable("shortUrl") @Valid @NotBlank shortUrl: String): ResponseEntity<String> {
        val originalUrl = urlService.resolveShortUrl(shortUrl)
        return ResponseEntity.ok(originalUrl)
    }
}
