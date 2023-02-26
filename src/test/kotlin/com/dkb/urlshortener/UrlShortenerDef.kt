package com.mobimeo.challenge

import com.mobimeo.challenge.model.CustomerWithAssets
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class CustomerWithAssetsDef : SpringContextConfiguration() {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java.toString())!!
        const val URL = "http://localhost:8080/customers/{externalId}"
    }

    var externalId: Int? = null
    var customerWithAssets: CustomerWithAssets? = null
    var webClient: WebClient = WebClient.create()

    @Before
    fun setup(scenario: Scenario) {
        log.info("Running Scenario : {}", scenario.name)
        this.externalId = 0
        this.customerWithAssets = null
    }

    @Given("Customer externalId {int}")
    fun customer_external_id(externalId: Int?) {
        if (externalId != null) {
            this.externalId = externalId
        }
    }

    @When("Fetch Customer With Assets for externalId")
    fun fetch_customer_with_assets_for_external_id() {
        log.info("Getting response for externalId : {}", this.externalId)
        var responseMono = webClient.get()
            .uri(URL, this.externalId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(CustomerWithAssets::class.java)

        this.customerWithAssets = responseMono.block()
        log.info("Got response : {}", this.customerWithAssets)
    }

    @Then("Assets size {int}")
    fun assets_size(size: Int?) {
        assert(this.customerWithAssets?.assets?.size == size)
    }

    @Then("Customer name is {string}")
    fun customer_name_is(name: String?) {
        assert(this.customerWithAssets?.customer?.name == name)
    }
}
