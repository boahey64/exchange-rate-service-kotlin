package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.account.model.Period
import com.boahey.exchangerateservice.account.model.UsageInfo
import com.boahey.exchangerateservice.account.service.UsageService
import com.boahey.exchangerateservice.exception.InvalidCurrencyException
import com.boahey.exchangerateservice.model.*
import com.boahey.exchangerateservice.util.convertDateString
import com.boahey.exchangerateservice.util.getCurrentDateString
import com.boahey.exchangerateservice.util.getRandomDateString
import com.google.gson.Gson
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Consumer

const val AVERAGE_DAYS_COUNT = 5

@Service
class ExchangeRateService(
        private val exchangeRatesApiClient: ExchangeRatesApiClient,
        private val validDaysService: ValidDaysService,
        private val trendAndAverageService: TrendAndAverageService,
        private val queryService: QueryService,
        private val validationService: ValidationService,
        private val usageService: UsageService
) {
    private var validCurrenciesSingleton: Set<String> = emptySet()

    @Throws(InvalidCurrencyException::class)
    fun getExchangeData(exchangeRateInput: ExchangeRateInput, customerId: String, randomQueryDate: Boolean): Optional<ExchangeRate>? {
        val queryDateString: HistoryDate = calculateHistoryDate(randomQueryDate)
        val exchangeRateValidationInput: ExchangeRateValidationInput = ExchangeRateValidationInput(
                exchangeRateInput.date,
                exchangeRateInput.baseCurrency,
                exchangeRateInput.targetCurrency,
                getValidCurrencies(),
                customerId,
                getUsageInfo(customerId, Period.MONTHLY, queryDateString),
                getUsageInfo(customerId, Period.YEARLY, queryDateString)
        )

        return if (validationService.checkExchangeRateInput(exchangeRateValidationInput)) {
            Optional.of<ExchangeRate>(calculateExchangeRate(
                    exchangeRateInput, customerId, convertDateString(queryDateString)))
        } else Optional.empty<ExchangeRate>()
    }

    fun getValidCurrencies(): Set<*> {
        if(validCurrenciesSingleton.isEmpty()) {
            val exchangeApiRates: ExchangeApiRates = getExchangeApiRates("latest", "USD")
            validCurrenciesSingleton = exchangeApiRates.rates.keys
        }
        return validCurrenciesSingleton
    }

    fun getExchangeRateHistory(startDate: String?, endDate: String?): ExchangeApiRatesHistory? {
        val exchangeApiRatesMono = exchangeRatesApiClient.getExchangeApiHistory(startDate!!, endDate!!)
        val exchangeApiRatesString = exchangeApiRatesMono!!.block()
        return convertJsonResponseToListOfExchangeApiRatesHistory(exchangeApiRatesString)
    }

    fun getUsageInfo(customerId: String?, period: Period, queryDateString: HistoryDate): UsageInfo {
        return if (period.equals(Period.MONTHLY)) {
            usageService.getMonthlyUsageInfo(queryDateString, customerId!!)
        } else usageService.getYearlyUsageInfo(queryDateString, customerId!!)
    }

    private fun calculateHistoryDate(randomQueryDate: Boolean): HistoryDate {
        return if (randomQueryDate) {
            getRandomDateString()
        } else
            getCurrentDateString()
    }

    private fun calculateExchangeRate(exchangeRateInput: ExchangeRateInput, customerId: String?, queryDate: Date): ExchangeRate {
        val nDaysExchangeRates = getNDaysExchangeRates(exchangeRateInput)
        val exchangeRate = ExchangeRate(
                calculateCurrentExchangeRate(exchangeRateInput),
                getAverageFiveDaysExchangeRate(nDaysExchangeRates),
                getExchangeRateTrend(nDaysExchangeRates)
        )
        queryService.saveQuery(exchangeRateInput, customerId!!, exchangeRate, queryDate)
        return exchangeRate
    }

    private fun calculateCurrentExchangeRate(exchangeRateInput: ExchangeRateInput): Double {
        val exchangeApiRates: ExchangeApiRates = getExchangeApiRates(exchangeRateInput.date, exchangeRateInput.baseCurrency)
        return exchangeApiRates.rates[exchangeRateInput.targetCurrency] ?: 0.0
    }

    private fun getAverageFiveDaysExchangeRate(nDaysExchangeRates: List<Double?>): Double {
        return trendAndAverageService.calculateAverageFiveDaysExchangeRate(nDaysExchangeRates)
    }

    private fun getNDaysExchangeRates(exchangeRateInput: ExchangeRateInput): List<Double> {
        val nDaysExchangeRates: MutableList<Double> = ArrayList()
        val validDaysBefore = validDaysService.calculateNValidDaysBackFrom(exchangeRateInput.date, AVERAGE_DAYS_COUNT)
        validDaysBefore.forEach(Consumer {
            day: String ->
            getExchangeApiRates(day, exchangeRateInput.baseCurrency).rates.get(exchangeRateInput.targetCurrency)?.let { nDaysExchangeRates.add(it) }
        }
        )
        return nDaysExchangeRates
    }

    private fun getExchangeRateTrend(nDaysExchangeRates: List<Double>): String {
        return trendAndAverageService.calculateExchangeRateTrend(nDaysExchangeRates)
    }

    private fun getExchangeApiRates(date: String, baseCurrency: String): ExchangeApiRates {
        val exchangeApiRatesMono = exchangeRatesApiClient.getExchangeApiRates(
                date, baseCurrency)
        val exchangeApiRatesString = exchangeApiRatesMono.block()
        return convertJsonResponseToExchangeApiRates(exchangeApiRatesString)
    }

    private fun convertJsonResponseToExchangeApiRates(clientResponse: String?): ExchangeApiRates {
        val g = Gson()
        return g.fromJson(clientResponse, ExchangeApiRates::class.java)
    }

    private fun convertJsonResponseToListOfExchangeApiRatesHistory(clientResponse: String?): ExchangeApiRatesHistory? {
        val g = Gson()
        return g.fromJson(clientResponse, ExchangeApiRatesHistory::class.java)
    }

}