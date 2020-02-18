package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateInput
import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.util.convertDateString
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class QueryServiceTest {

    @Mock
    private val exchangeRateDataService: ExchangeRateDataService? = null

    private var serviceUnderTest: QueryService? = null

    var currentDate: Date = Date()


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        serviceUnderTest = QueryService(exchangeRateDataService!!)
        val exchangeRateQuery = ExchangeRateQuery(
                0,
                convertDateString("2019-03-15"),
                "fakeUser",
                "2019-03-15", "EUR", "PHP",
                4.324, 4.29, "undefined",
                currentDate
        )

        doReturn(exchangeRateQuery)
                .whenever(exchangeRateDataService).add(any())

    }

    @Test
    fun add_query_with_current_query_date() {
        val exchangeRateQuery = serviceUnderTest!!.saveQuery(
                ExchangeRateInput("2019-03-15", "EUR", "PHP"),
                "fakeUser",
                ExchangeRate(4.324, 4.29, "undefined"),
                currentDate
        )
        assertEquals(exchangeRateQuery!!.queryDate, currentDate)
        assertEquals(exchangeRateQuery.startDate, convertDateString("2019-03-15"))
        assertEquals(exchangeRateQuery.baseCurrency, "EUR")
        assertEquals(exchangeRateQuery.targetCurrency, "PHP")
        assertEquals(exchangeRateQuery.customerId, "fakeUser")
        assertEquals(exchangeRateQuery.averageRate, 4.29)
        assertEquals(exchangeRateQuery.currentRate, 4.324)
        assertEquals(exchangeRateQuery.trend, "undefined")
    }

}