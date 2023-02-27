package com.dkb.urlshortener.configuration

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class DatabaseConfiguration {

    // We don't need to have this configuration for MVP as we use secrets to define the config
    // We use spring boot, just need to set up spring.r2dbc.url, spring.r2dbc.username, spring.r2dbc.password properties,
    // and it will autoconfigure ConnectionFactory and DatabaseClient

    // Here I have created just as an example to be overridden in future if needed and to have separate connectionFactory and client for Write/Read instance

    @Value("\${database.host}")
    lateinit var host: String
    @Value("\${database.port}")
    lateinit var port: Integer
    @Value("\${database.name}")
    lateinit var name: String
    @Value("\${database.username}")
    lateinit var username: String
    @Value("\${database.password}")
    lateinit var password: String

    @Bean
    fun connectionFactory(): PostgresqlConnectionFactory {
        val config = PostgresqlConnectionConfiguration.builder() //
            .host(host)
            .port(port.toInt())
            .database(name)
            .username(username)
            .password(password)
            .build()

        return PostgresqlConnectionFactory(config)
    }

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory?): DatabaseClient? {
        return DatabaseClient.builder()
            .connectionFactory(connectionFactory!!)
            .namedParameters(true)
            .build()
    }

    @Bean // this Transaction Manager with the primary database config
    fun transactionManager(connectionFactory: ConnectionFactory?): ReactiveTransactionManager? {
        return R2dbcTransactionManager(connectionFactory!!)
    }

    @Bean
    @Profile("prod")
    @Qualifier("ReadTransactionManager") // We can use this Transaction Manager with the secondary database config
    fun readTransactionManager(connectionFactory: ConnectionFactory?): ReactiveTransactionManager? {
        return R2dbcTransactionManager(connectionFactory!!)
    }
}
