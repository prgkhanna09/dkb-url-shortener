package com.dkb.urlshortener.model

import java.io.Serializable

class UrlCache(val originalUrl: String, val shortUrl: String) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
