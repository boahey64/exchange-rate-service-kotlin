package com.boahey.exchangerateservice.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import reactor.netty.http.HttpResources
import reactor.test.StepVerifier

class ExchangeRatesApiClientTest {

//    @get:Rule
//    val wireMockRule = WireMockRule(WireMockConfiguration.wireMockConfig().dynamicPort())

    private lateinit var serviceUnderTest: ExchangeRatesApiClient


    companion object {
        private val WIREMOCK_SERVER = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            WIREMOCK_SERVER.start()
        }
        @AfterAll
        @JvmStatic
        fun afterAll() {
            WIREMOCK_SERVER.stop()
        }
    }



    @BeforeEach
    fun setUp() {
        HttpResources.reset()
//        serviceUnderTest = ExchangeRatesApiClient("http://localhost:${wireMockRule.port()}" );
        serviceUnderTest = ExchangeRatesApiClient("http://localhost:${WIREMOCK_SERVER.port()}" );
    }

    @Test
    fun fetch_2019_12_31_base_EUR() { // given
        aResponseWithStatusCode(200, 0, "2019-12-31", "EUR")
        // when
        val response = serviceUnderTest!!.getExchangeApiRates("2019-12-31", "EUR")
        // then
        StepVerifier.create(response).expectNext(anExchangeApiRates()).verifyComplete()
    }

    @Test
    fun fetch_2019_12_31_base_CAD() { // given
        aResponseWithStatusCode(200, 0, "2019-12-31", "CAD")
        // when
        val response = serviceUnderTest!!.getExchangeApiRates("2019-12-31", "CAD")
        // then
        StepVerifier.create(response).expectNext(anExchangeApiRates()).verifyComplete()
    }


    @Test
    fun timeout_returns_erroneous_mono() { // given
        aResponseWithStatusCode(200, 4000, "2019-12-31", "CAD")
        // when
        val response = serviceUnderTest!!.getExchangeApiRates("2019-12-31", "CAD")
        // then
        StepVerifier.create(response).expectError().verify()
    }

    private fun anExchangeApiRates(): String? {
        return "{\"rates\":{\"CAD\":1.4598,\"HKD\":8.7473,\"ISK\":135.8,\"PHP\":56.9,\"DKK\":7.4715},\"base\":\"EUR\",\"date\":\"2019-12-31\"}"
    }

    private fun aResponseWithStatusCode(httpStatus: Int, delay: Int, date: String, base: String) {
        WIREMOCK_SERVER.stubFor(WireMock.get(WireMock.urlPathMatching("/$date"))
                .withQueryParam("base", WireMock.equalTo(base))
                .willReturn(WireMock.aResponse()
                        .withStatus(httpStatus)
                        .withFixedDelay(delay)
                        .withBody(anExchangeApiRates())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                )
        )
    }

}