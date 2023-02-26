package com.dkb.urlshortener.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

// @Entity
// @Table(
//    name = "url",
//    indexes = [Index(name = "url_short_url_idx", columnList = "short_url")],
//    uniqueConstraints = [
//        UniqueConstraint(
//            name = "url_short_url_constraint",
//            columnNames = [
//                "short_url"
//            ]
//        ),
//        UniqueConstraint(
//            name = "url_original_short_url_contraint",
//            columnNames = [
//                "original_url", "short_url"
//            ]
//        )
//    ]
// )
@Table
data class Url(
    @Id
    val id: Long? = null,
    @Column("original_url")
    val originalUrl: String,
    @Column("short_url")
    val shortUrl: String,
    @CreatedDate
    val createdAt: LocalDateTime? = null
)
