package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRateTrend
import org.springframework.stereotype.Component
import java.text.DecimalFormat

@Component
class TrendAndAverageService {
    val AVERAGE_DAYS_COUNT = 5

    fun calculateAverageFiveDaysExchangeRate(exchangeRates: List<Double?>): Double? {
        val df = DecimalFormat("#.0000000000")
        return exchangeRates.stream()
                .mapToDouble { i: Double? -> df.format(i).toDouble() }
                .average()
                .asDouble
    }

    fun calculateExchangeRateTrend(exchangeRates: List<Double>): String? {
        return if (isAscending(exchangeRates)) ExchangeRateTrend.ASCENDING.label else if (isDescending(exchangeRates)) ExchangeRateTrend.DESCENDING.label else if (isConstant(exchangeRates)) ExchangeRateTrend.CONSTANT.label else ExchangeRateTrend.UNDEFINED.label
    }

    private fun isAscending(exchangeRates: List<Double>): Boolean {
        for (i in 0 until exchangeRates.size - 1) {
            if (exchangeRates[i] <= exchangeRates[i + 1]) {
                return false
            }
        }
        return true
    }

    private fun isDescending(exchangeRates: List<Double>): Boolean {
        for (i in 0 until exchangeRates.size - 1) {
            if (exchangeRates[i] >= exchangeRates[i + 1]) {
                return false
            }
        }
        return true
    }

    private fun isConstant(exchangeRates: List<Double>): Boolean {
        for (i in 0 until exchangeRates.size - 1) {
            if (exchangeRates[i] != exchangeRates[i + 1]) {
                return false
            }
        }
        return true
    }

}