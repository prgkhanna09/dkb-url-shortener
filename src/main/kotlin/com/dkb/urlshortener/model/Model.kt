package com.dkb.urlshortener.model

import com.dkb.urlshortener.utils.validation.ValidUrl
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class ShortenUrlRequest(@NotNull @Valid @ValidUrl val url: String)

data class ShortenUrlResponse(val url: String)
