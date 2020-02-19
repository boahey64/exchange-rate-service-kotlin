package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.account.model.UsageInfo
import com.boahey.exchangerateservice.account.service.FREE_MAX_ALLOWED_QUERIES_PER_MONTH
import com.boahey.exchangerateservice.account.service.FREE_MAX_ALLOWED_QUERIES_PER_YEAR
import com.boahey.exchangerateservice.exception.ExceededAllowedQueriesException
import com.boahey.exchangerateservice.exception.InvalidCurrencyException
import com.boahey.exchangerateservice.exception.InvalidDateException
import com.boahey.exchangerateservice.exception.InvalidDateFormatException
import com.boahey.exchangerateservice.model.ExchangeApiRates
import com.boahey.exchangerateservice.model.ExchangeRateValidationInput
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ValidationServiceTest {

    private val serviceUnderTest = ValidationService()


    @Test
    fun pass_validation() {
        val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                "2019-12-31", "EUR", "CAD", 1L, 1L)
        val actual = serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        Assertions.assertTrue(actual)
    }

    @Test
    fun call_with_exceeding_monthly_query_quota_for_an_account() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "2019-12-31", "EUR", "CAD", 5L, 5L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is ExceededAllowedQueriesException)
    }

    @Test
    fun call_with_exceeding_yearly_query_quota_for_an_account() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "2019-12-31", "EUR", "CAD", 4L, 7L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is ExceededAllowedQueriesException)
    }

    @Test
    fun call_with_invalid_base_currency() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "2019-12-31", "XXX", "CAD", 1L, 1L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is InvalidCurrencyException)
    }

    @Test
    fun call_with_invalid_target_currency() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "2019-12-31", "EUR", "XXX", 1L, 1L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is InvalidCurrencyException)
    }

    @Test
    fun call_with_invalid_date_format() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "2019/12/31", "EUR", "USD", 1L, 1L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is InvalidDateFormatException)
    }

    @Test
    fun call_with_date_before_2000_01_01() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    "1999-12-31", "EUR", "USD", 1L, 1L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is InvalidDateException)
    }

    @Test
    fun call_with_date_after_yesterday() {
        var e: Throwable? = null
        try {
            val exchangeRateValidationInput: ExchangeRateValidationInput = anExchangeRateValidationInput(
                    getCurrentDateString(), "EUR", "USD", 1L, 1L)
            serviceUnderTest.checkExchangeRateInput(exchangeRateValidationInput)
        } catch (ex: Throwable) {
            e = ex
        }
        Assertions.assertTrue(e is InvalidDateException)
    }

    private fun getCurrentDateString(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return today.format(formatter)
    }

    private fun getValidCurrencies(): Set<*> {
        val exchangeApiRates: ExchangeApiRates = usdBasedExchangeApiRates()
        return exchangeApiRates.rates.keys
    }

    private fun usdBasedExchangeApiRates(): ExchangeApiRates {
        val g = Gson()
        val jsonString = "{\"rates\":{\"CAD\":1.3072159397,\"HKD\":7.7715849937,\"ISK\":122.5991742955,\"PHP\":50.7278765033,\"DKK\":6.7067851373,\"HUF\":298.8152934841,\"CZK\":22.5668641178,\"GBP\":0.7690091546,\"RON\":4.2903428469,\"SEK\":9.4767546222,\"IDR\":13697.9895889427,\"INR\":70.7974331359,\"BRL\":4.1599353796,\"RUB\":61.4239813319,\"HRK\":6.6812959971,\"JPY\":109.8815293484,\"THB\":30.239633818,\"CHF\":0.964907557,\"EUR\":0.8975049363,\"MYR\":4.0754801651,\"BGN\":1.7553401544,\"TRY\":5.8858373721,\"CNY\":6.887991384,\"NOK\":8.8691437803,\"NZD\":1.5172320948,\"ZAR\":14.379644588,\"USD\":1.0,\"MXN\":18.8064979357,\"SGD\":1.3472446598,\"AUD\":1.4528809908,\"ILS\":3.4608687848,\"KRW\":1157.0813139472,\"PLN\":3.7933046132},\"base\":\"USD\",\"date\":\"2020-01-15\"}"
        return g.fromJson(jsonString, ExchangeApiRates::class.java)
    }


    private fun anExchangeRateValidationInput(
            date: String, baseCurrency: String, targetCurrency: String, monthlyQueryCount: Long, yearlyQueryCount: Long): ExchangeRateValidationInput {
        return ExchangeRateValidationInput(
                date,
                baseCurrency,
                targetCurrency,
                getValidCurrencies(),
                "fakeUser",
                UsageInfo(monthlyQueryCount, FREE_MAX_ALLOWED_QUERIES_PER_MONTH),
                UsageInfo(yearlyQueryCount, FREE_MAX_ALLOWED_QUERIES_PER_YEAR)
                )
    }

}