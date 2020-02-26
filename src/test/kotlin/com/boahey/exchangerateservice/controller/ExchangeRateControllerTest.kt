package com.boahey.exchangerateservice.controller

import com.boahey.exchangerateservice.account.service.UsageService
import com.boahey.exchangerateservice.exception.CustomErrorResponse
import com.boahey.exchangerateservice.model.ExchangeApiRatesHistory
import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.service.ExchangeRateService
import com.boahey.exchangerateservice.service.HistoryService
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
internal class ExchangeRateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var exchangeRateService: ExchangeRateService

    @MockBean
    lateinit var historyService: HistoryService

    @MockBean
    lateinit var usageService: UsageService

    private val path = "/api/exchange-rate"


    @Test
    fun get_currency_rate() {
        doReturn(Optional.of(ExchangeRate(12.4, 12.7, "ascending")))
                .whenever(exchangeRateService).getExchangeData(any(), any(), any())

        mockMvc.perform(MockMvcRequestBuilders.get("$path/2019-12-31/EUR/PHP"))
                .andExpect {
                    status().isOk
                    jsonPath("currentRate", equalTo(12.4))
                    jsonPath("averageRate", equalTo(12.7))
                    jsonPath("trend", equalTo("ascending"))

                }
    }

    @Test
    fun get_currency_rate_throws_not_expected_error() {
        Mockito.`when`(exchangeRateService.getExchangeData(any(), any(), any())).thenReturn(Optional.empty())

        val mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$path/2019-12-31/EUR/PHP"))
                .andExpect(status().is4xxClientError)
                .andReturn()
        val expectedErrorResponse = CustomErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Not expected error.",
        "Not expected error.")

        checkAssertionsForWrongParameter(mvcResult, expectedErrorResponse)
    }


    @Test
    fun get_daily_local_history() {
        Mockito.`when`(historyService.getDailyExchangeRateQueriesLocal(any())).thenReturn(aHistoricalLocalExchangeRateList())

        mockMvc.perform(MockMvcRequestBuilders.get("$path/history/local/daily/2019/12/31"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("[0].baseCurrency", equalTo("EUR")))
                .andExpect(jsonPath("[0].targetCurrency", equalTo("CHF")))
                .andExpect(jsonPath("[0].currentRate", equalTo(1.0854)))
                .andExpect(jsonPath("[0].averageRate", equalTo(1.0879)))
                .andExpect(jsonPath("[0].trend", equalTo("undefined")))
    }


    @Test
    fun get_monthly_local_history() {
        Mockito.`when`(historyService.getMonthlyExchangeRateQueriesLocal(any())).thenReturn(aHistoricalLocalExchangeRateList())
        mockMvc.perform(MockMvcRequestBuilders.get("$path/history/local/monthly/2020/01"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("[0].baseCurrency", equalTo("EUR")))
                .andExpect(jsonPath("[0].targetCurrency", equalTo("CHF")))
                .andExpect(jsonPath("[0].currentRate", equalTo(1.0854)))
                .andExpect(jsonPath("[0].averageRate", equalTo(1.0879)))
                .andExpect(jsonPath("[0].trend", equalTo("undefined")))
    }


    @Test
    fun get_daily_history() {
        Mockito.`when`(historyService.getDailyExchangeApiRates(any())).thenReturn(aHistoricalExchangeRateList())
        mockMvc.perform(MockMvcRequestBuilders.get("$path/history/daily/2019/12/31"))
                .andExpect {
                    status().isOk
                    content().contentType(MediaType.APPLICATION_JSON)
//                    content { jsonPath( "$", equalTo("{}"))
                    }
//                .andExpect(MockMvcResultMatchers.status().isOk)
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", IsEqual.equalTo(4)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.baseCurrency", IsEqual.equalTo(4)))
//                .andExpect(MockMvcResultMatchers.content().string("{}"))
    }

//    @Test
//    fun get_monthly_history() {
//        Mockito.`when`(historyService.getMonthlyExchangeApiRates(any())).thenReturn(aHistoricalExchangeRateList())
//
//        mockMvc.perform(MockMvcRequestBuilders.get("$path/history/monthly/2020/01"))
//                .andExpect(MockMvcResultMatchers.status().isOk)
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", IsEqual.equalTo(4)))
//    }

    private fun aHistoricalLocalExchangeRateList(): List<ExchangeRateQuery>? {
        val jsonString = "  {\n" +
                "    \"startDate\": \"2019-12-30T23:00:00.000+0000\",\n" +
                "    \"dateString\": \"2019-12-31\",\n" +
                "    \"baseCurrency\": \"EUR\",\n" +
                "    \"targetCurrency\": \"CHF\",\n" +
                "    \"currentRate\": 1.0854,\n" +
                "    \"averageRate\": 1.0879,\n" +
                "    \"trend\": \"undefined\"\n" +
                "  }\n"
        val g = Gson()
        val exchangeRateQuery: ExchangeRateQuery = g.fromJson(jsonString, ExchangeRateQuery::class.java)
        return Arrays.asList<ExchangeRateQuery>(exchangeRateQuery)
    }

    private fun aHistoricalExchangeRateList(): ExchangeApiRatesHistory? {
        val jsonString = "{\n" +
                "  \"rates\": {\n" +
                "    \"2019-12-31\": {\n" +
                "      \"CAD\": 1.4598,\n" +
                "      \"HKD\": 8.7473,\n" +
                "      \"ISK\": 135.8,\n" +
                "      \"PHP\": 56.9,\n" +
                "      \"DKK\": 7.4715,\n" +
                "      \"HUF\": 330.53,\n" +
                "      \"CZK\": 25.408,\n" +
                "      \"AUD\": 1.5995,\n" +
                "      \"RON\": 4.783,\n" +
                "      \"SEK\": 10.4468,\n" +
                "      \"IDR\": 15595.6,\n" +
                "      \"INR\": 80.187,\n" +
                "      \"BRL\": 4.5157,\n" +
                "      \"RUB\": 69.9563,\n" +
                "      \"HRK\": 7.4395,\n" +
                "      \"JPY\": 121.94,\n" +
                "      \"THB\": 33.415,\n" +
                "      \"CHF\": 1.0854,\n" +
                "      \"SGD\": 1.5111,\n" +
                "      \"PLN\": 4.2568,\n" +
                "      \"BGN\": 1.9558,\n" +
                "      \"TRY\": 6.6843,\n" +
                "      \"CNY\": 7.8205,\n" +
                "      \"NOK\": 9.8638,\n" +
                "      \"NZD\": 1.6653,\n" +
                "      \"ZAR\": 15.7773,\n" +
                "      \"USD\": 1.1234,\n" +
                "      \"MXN\": 21.2202,\n" +
                "      \"ILS\": 3.8845,\n" +
                "      \"GBP\": 0.8508,\n" +
                "      \"KRW\": 1296.28,\n" +
                "      \"MYR\": 4.5953\n" +
                "    }\n" +
                "  },\n" +
                "  \"start_at\": \"2019-12-31\",\n" +
                "  \"base\": \"EUR\",\n" +
                "  \"end_at\": \"2020-01-01\"\n" +
                "}"
        val g = Gson()
        return g.fromJson(jsonString, ExchangeApiRatesHistory::class.java)
    }

}