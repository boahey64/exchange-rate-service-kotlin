package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRateTrend
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.MatcherAssert.assertThat

import org.hamcrest.Matchers.`is`


import org.junit.jupiter.api.Test
import java.util.*

internal class TrendAndAverageServiceTest {

    private val serviceUnderTest = TrendAndAverageService()

    @Test
    fun calculateAverageFiveDaysExchangeRate() {
        val average = serviceUnderTest.calculateAverageFiveDaysExchangeRate(
                Arrays.asList(*arrayOf(3.0, 4.0, 2.0, 1.0, 1.0))
        )
        MatcherAssert.assertThat(average, Matchers.`is`(2.2))
    }

    @Test
    fun calculate_ascending_exchange_rate_trend() {
        val trend = serviceUnderTest.calculateExchangeRateTrend(
                Arrays.asList(*arrayOf(1.55, 1.54, 1.53, 1.52, 1.51))
        )
        assertThat(trend, `is`(ExchangeRateTrend.ASCENDING.label))
    }

    @Test
    fun calculate_descending_exchange_rate_trend() {
        val trend = serviceUnderTest.calculateExchangeRateTrend(
                Arrays.asList(*arrayOf(1.51, 1.52, 1.53, 1.54, 1.55))
        )
        assertThat(trend, `is`(ExchangeRateTrend.DESCENDING.label))
    }

    @Test
    fun calculate_constant_exchange_rate_trend() {
        val trend = serviceUnderTest.calculateExchangeRateTrend(
                Arrays.asList(*arrayOf(1.5, 1.5, 1.5, 1.5, 1.5))
        )
        assertThat(trend, `is`(ExchangeRateTrend.CONSTANT.label))
    }

    @Test
    fun calculate_undefined_exchange_rate_trend() {
        val trend = serviceUnderTest.calculateExchangeRateTrend(
                Arrays.asList(*arrayOf(1.5, 1.51, 1.5, 1.5, 1.5))
        )
        assertThat(trend, `is`(ExchangeRateTrend.UNDEFINED.label))
    }

}