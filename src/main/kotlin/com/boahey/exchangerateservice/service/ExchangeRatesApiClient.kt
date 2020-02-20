package com.boahey.exchangerateservice.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class ExchangeRatesApiClient(
        @Value("\${exchange.rates.api.url}") val exchangeRatesApiUrl: String
) {
    private val log = LoggerFactory.getLogger(ExchangeRatesApiClient::class.java)

    private val exchangeRatesApiClient = WebClient
            .builder()
            .baseUrl(exchangeRatesApiUrl)
            .build()


    fun getExchangeApiHistory(startDate: String, endDate: String): Mono<String?>? {
        val uri = calculateHistoryUrl(startDate, endDate)
        log.debug("uri: {}", uri)
        return getExchangeRateApiCall(uri)
    }

    fun getExchangeApiRates(date: String, base: String): Mono<String?> {
        val uri = calculateUrl(date, base)
        log.debug("uri: {}", uri)
        return getExchangeRateApiCall(uri)
    }

    private fun getExchangeRateApiCall(uri: String): Mono<String?> {
        return exchangeRatesApiClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .timeout(Duration.ofSeconds(3))
                .doOnError { ex: Throwable? -> log.debug("onError: $ex") }
                .doOnSuccess { clientResponse: String? -> log.debug("onSuccess: {}", clientResponse) }
    }

    private fun calculateUrl(date: String, base: String): String {
        val uri = "$exchangeRatesApiUrl/$date"
        return "$uri?base=$base"
    }

    private fun calculateHistoryUrl(startDate: String, endDate: String): String {
        return "$exchangeRatesApiUrl/history?start_at=$startDate&end_at=$endDate"
    }

}