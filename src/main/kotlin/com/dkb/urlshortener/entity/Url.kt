package com.dkb.urlshortener.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.persistence.Version
import java.time.LocalDateTime

@Entity
@Table(
    name = "url",
    indexes = [Index(name = "url_short_url_idx", columnList = "short_url")],
    uniqueConstraints = [
        UniqueConstraint(
            name = "url_short_url_constraint",
            columnNames = [
                "short_url"
            ]
        ),
        UniqueConstraint(
            name = "url_original_short_url_contraint",
            columnNames = [
                "original_url", "short_url"
            ]
        )
    ]
)
data class Url(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true, name = "original_url")
    val originalUrl: String,
    @Column(unique = true, name = "short_url")
    val shortUrl: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Version
    val version: Long? = null
)
