package com.boahey.exchangerateservice.controller

import com.boahey.exchangerateservice.account.service.UsageService
import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateInput
import com.boahey.exchangerateservice.service.ExchangeRateService
import com.boahey.exchangerateservice.service.HistoryService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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
    @Throws(Exception::class)
    fun get_currency_rate() {
        doReturn(Optional.of(ExchangeRate(12.4, 12.7, "ascending")))
                .whenever(exchangeRateService).getExchangeData(any(), any(), any())

        mockMvc.perform(MockMvcRequestBuilders.get("$path/2019-12-31/EUR/PHP"))
                .andExpect {
                    MockMvcResultMatchers.status().isOk
                    MockMvcResultMatchers.jsonPath("currentRate", IsEqual.equalTo(12.4))
                    MockMvcResultMatchers.jsonPath("averageRate", IsEqual.equalTo(12.7))
                    MockMvcResultMatchers.jsonPath("trend", IsEqual.equalTo("ascending"))

                }

    }

}