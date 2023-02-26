package com.dkb.urlshortener.model

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class ShortenUrlRequest(@NotNull @Valid val url: String)

data class ShortenUrlResponse(val url: String)
