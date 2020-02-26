package com.boahey.exchangerateservice.controller

import com.boahey.exchangerateservice.exception.CustomErrorResponse
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
class ExchangeRateControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc


    private val path = "/api/exchange-rate"

    @Test
    fun get_currency_rate_wrong_target_currency() {
        val mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$path/2019-12-31/EUR/XXX"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andReturn()
        val expectedErrorResponse = CustomErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Target currency XXX is not valid",
        "You try to fetch the exchange rate for a not supported currency."
        )
        checkAssertionsForWrongParameter(mvcResult, expectedErrorResponse)
    }

    @Test
    fun get_currency_rate_wrong_base_currency() {
        val mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$path/2019-12-31/XXX/PHP"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andReturn()
        val expectedErrorResponse = CustomErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Base currency XXX is not valid",
        "You try to fetch the exchange rate for a not supported currency."
        )
        checkAssertionsForWrongParameter(mvcResult, expectedErrorResponse)
    }

    @Test
    fun get_currency_rate_wrong_date() {
        val mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$path/1999-12-31/EUR/PHP"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andReturn()
        val expectedErrorResponse = CustomErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Date is out of range: 1999-12-31",
        "The date has to be between 2000-01-01 and yesterday."
        )
        checkAssertionsForWrongParameter(mvcResult, expectedErrorResponse)
    }

}