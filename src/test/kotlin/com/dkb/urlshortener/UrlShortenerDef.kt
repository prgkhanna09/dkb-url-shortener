package com.dkb.urlshortener

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class UrlShortenerDef : SpringContextConfiguration() {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java.toString())!!
        const val URL = "http://localhost:8080/api/urls?shortUrl="
    }

    var shortUrl: String? = null
    var originalUrl: String? = null
    var webClient: WebClient = WebClient.create()

    @Before
    fun setup(scenario: Scenario) {
        log.info("Running Scenario : {}", scenario.name)
        this.shortUrl = ""
        this.originalUrl = ""
    }

    @Given("Short url is {string}")
    fun hash(shortUrl: String?) {
        if (shortUrl != null) {
            this.shortUrl = shortUrl
        }
    }

    @When("Resolve URL is called")
    fun resolve_url_is_called() {
        log.info("Getting response for hash : {}", this.shortUrl)
        var responseMono = webClient.get()
            .uri(URL + this.shortUrl)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String::class.java)

        this.originalUrl = responseMono.block()
        log.info("Got response : {}", this.originalUrl)
    }

    @Then("Original URL is {string}")
    fun original_url_is(url: String?) {
        assert(this.originalUrl == url)
    }
}
