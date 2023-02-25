package com.dkb.urlshortener.repository

import com.dkb.urlshortener.entity.Url
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

// Here again for the data layer we have an interface,
// in future if we want to use any other data source we just replace the implementation
@Repository
interface UrlRepository : ReactiveCrudRepository<Url, Long> {
    fun findByShortUrl(shortUrl: String): Url?
}