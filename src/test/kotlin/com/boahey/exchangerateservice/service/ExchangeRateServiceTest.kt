package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.account.model.UsageInfo
import com.boahey.exchangerateservice.account.service.UsageService
import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateInput
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.Mono
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeRateServiceTest {
    @Mock
    private var exchangeRatesApiClient: ExchangeRatesApiClient? = null
    @Mock
    private var validDaysService: ValidDaysService? = null
    @Mock
    private var trendAndAverageService: TrendAndAverageService? = null
    @Mock
    private var queryService: QueryService? = null
    @Mock
    private var validationService: ValidationService? = null
    @Mock
    private var usageService: UsageService? = null

    private var serviceUnderTest: ExchangeRateService? = null

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        serviceUnderTest = ExchangeRateService(
                exchangeRatesApiClient!!, validDaysService!!, trendAndAverageService!!,
                queryService!!, validationService!!, usageService!!)

//        Mockito.`when`(validationService!!.checkExchangeRateInput(ArgumentMatchers.any())).thenReturn(true)
        doReturn(true)
                .whenever(validationService)!!.checkExchangeRateInput(any())

        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("latest", "USD")).thenReturn(Mono.just(anExchangeApiRatesStringWithBaseUsd()))
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-31", "USD")).thenReturn(Mono.just(anExchangeApiRatesStringWithBaseUsd()))
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-31", "EUR")).thenReturn(Mono.just(anExchangeApiRatesStringWithBaseEur()))

        lastFiveDaysMock("EUR", 1.567, 1.556, 1.542, 1.539, 1.529)

        Mockito.`when`(validDaysService!!.calculateNValidDaysBackFrom("2019-12-31", 5)).thenReturn(
                Arrays.asList(*arrayOf("2019-12-30", "2019-12-27", "2019-12-26", "2019-12-25", "2019-12-24")))

        Mockito.`when`(trendAndAverageService!!.calculateAverageFiveDaysExchangeRate(any())).thenReturn(1.5466)
        Mockito.`when`(trendAndAverageService!!.calculateExchangeRateTrend(any())).thenReturn("increasing")

        Mockito.`when`(usageService!!.getYearlyUsageInfo(any(), any())).thenReturn(UsageInfo(3L, 4L))
        Mockito.`when`(usageService!!.getMonthlyUsageInfo(any(), any())).thenReturn(UsageInfo(3L, 4L))
    }

    @Test
    fun call_with_valid_default_base_currency() {
        val expected = ExchangeRate(1.4598, 1.5466, "increasing")
        val actual: Optional<ExchangeRate>? = serviceUnderTest!!.getExchangeData(ExchangeRateInput(
                "2019-12-31", "EUR", "CAD"), "fakeUser", false)
        MatcherAssert.assertThat(actual!!.get(), Matchers.`is`<Any>(expected))
    }

    @Test
    fun call_with_valid_base_currency_USD() {
        lastFiveDaysMock("USD", 1.367, 1.356, 1.342, 1.339, 1.329)
        Mockito.`when`(trendAndAverageService!!.calculateAverageFiveDaysExchangeRate(any())).thenReturn(1.3466)
        val expected = ExchangeRate(1.3072159397, 1.3466, "increasing")
        val actual: Optional<ExchangeRate>? = serviceUnderTest!!.getExchangeData(ExchangeRateInput(
                "2019-12-31", "USD", "CAD"), "fakeUser", false)
        MatcherAssert.assertThat(actual!!.get(), Matchers.`is`<Any>(expected))
    }

    private fun anExchangeApiRatesStringWithBaseUsd(): String {
        return "{\"rates\":{\"CAD\":1.3072159397,\"HKD\":7.7715849937,\"ISK\":122.5991742955,\"PHP\":50.7278765033,\"DKK\":6.7067851373,\"HUF\":298.8152934841,\"CZK\":22.5668641178,\"GBP\":0.7690091546,\"RON\":4.2903428469,\"SEK\":9.4767546222,\"IDR\":13697.9895889427,\"INR\":70.7974331359,\"BRL\":4.1599353796,\"RUB\":61.4239813319,\"HRK\":6.6812959971,\"JPY\":109.8815293484,\"THB\":30.239633818,\"CHF\":0.964907557,\"EUR\":0.8975049363,\"MYR\":4.0754801651,\"BGN\":1.7553401544,\"TRY\":5.8858373721,\"CNY\":6.887991384,\"NOK\":8.8691437803,\"NZD\":1.5172320948,\"ZAR\":14.379644588,\"USD\":1.0,\"MXN\":18.8064979357,\"SGD\":1.3472446598,\"AUD\":1.4528809908,\"ILS\":3.4608687848,\"KRW\":1157.0813139472,\"PLN\":3.7933046132},\"base\":\"USD\",\"date\":\"2020-01-15\"}"
    }

    private fun anExchangeApiRatesStringWithBaseEur(): String {
        return "{\"rates\":{\"CAD\":1.4598,\"HKD\":8.7473,\"ISK\":135.8,\"PHP\":56.9,\"DKK\":7.4715,\"HUF\":330.53,\"CZK\":25.408,\"AUD\":1.5995,\"RON\":4.783,\"SEK\":10.4468,\"IDR\":15595.6,\"INR\":80.187,\"BRL\":4.5157,\"RUB\":69.9563,\"HRK\":7.4395,\"JPY\":121.94,\"THB\":33.415,\"CHF\":1.0854,\"SGD\":1.5111,\"PLN\":4.2568,\"BGN\":1.9558,\"TRY\":6.6843,\"CNY\":7.8205,\"NOK\":9.8638,\"NZD\":1.6653,\"ZAR\":15.7773,\"USD\":1.1234,\"MXN\":21.2202,\"ILS\":3.8845,\"GBP\":0.8508,\"KRW\":1296.28,\"MYR\":4.5953},\"base\":\"EUR\",\"date\":\"2019-12-31\"}"
    }

    private fun aSimpleVariableExchangeApiRatesString(date: String, targetCurrency: String, targetValue: Double): String? {
        return "{\"rates\":{\"$targetCurrency\":$targetValue},\"base\":\"EUR\",\"date\":\"$date\"}"
    }


    private fun lastFiveDaysMock(
            baseCurrency: String, currencyValue1: Double, currencyValue2: Double, currencyValue3: Double, currencyValue4: Double, currencyValue5: Double) {
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-30", baseCurrency)).thenReturn(aSimpleVariableExchangeApiRatesString("2019-12-30", "CAD", currencyValue1)?.let {
            Mono.just(
                    it)
        })
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-27", baseCurrency)).thenReturn(aSimpleVariableExchangeApiRatesString("2019-12-27", "CAD", currencyValue2)?.let {
            Mono.just(
                    it)
        })
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-26", baseCurrency)).thenReturn(aSimpleVariableExchangeApiRatesString("2019-12-26", "CAD", currencyValue3)?.let {
            Mono.just(
                    it)
        })
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-25", baseCurrency)).thenReturn(aSimpleVariableExchangeApiRatesString("2019-12-25", "CAD", currencyValue4)?.let {
            Mono.just(
                    it)
        })
        Mockito.`when`(exchangeRatesApiClient!!.getExchangeApiRates("2019-12-24", baseCurrency)).thenReturn(aSimpleVariableExchangeApiRatesString("2019-12-24", "CAD", currencyValue5)?.let {
            Mono.just(
                    it)
        })
    }

}