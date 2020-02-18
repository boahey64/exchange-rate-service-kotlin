package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateInput
import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.repository.ExchangeRateRepository
import com.boahey.exchangerateservice.util.convertDateString
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import org.hamcrest.MatcherAssert.assertThat

import org.hamcrest.Matchers.`is`


@SpringBootTest
class ExchangeRateDataServiceIT {
    @Autowired
    private lateinit var exchangeRateRepository: ExchangeRateRepository

    @Autowired
    private lateinit var exchangeRateDataService: ExchangeRateDataService

    @BeforeEach
    fun setUp() {
        exchangeRateRepository.deleteAll()
    }

    @Test
    fun find_queries_between_start_and_end_date() {
        prepareDatabase()
        assertThat(exchangeRateDataService.findByStartDateBetween(
                convertDateString("2019-12-31"),
                convertDateString("2020-01-01")
        )!!.size, Matchers.`is`(2))
    }

    @Test
    fun count_queries_with_query_date_of_customer_id_between_start_and_end() {
        prepareDatabase()
        prepareDatabaseWithOtherQueries()
        assertThat(exchangeRateDataService.countByCustomerIdAndQueryDateBetween(
                "COUNT_USER",
                convertDateString("2020-01-01"),
                convertDateString("2020-01-01")
        ), Matchers.`is`(2L))
    }

    private fun prepareDatabase() {
        val exchangeRateQuery1 = ExchangeRateQuery(
                0,
                convertDateString("2019-12-31"),
                "FAKE_USER",
                "2019-12-31", "EUR", "CAD",
                15.3, 17.3, "decreasing",
                convertDateString("2019-12-31")
        )
        exchangeRateRepository.save(exchangeRateQuery1)
        val exchangeRateQuery2 = ExchangeRateQuery(
                0,
                convertDateString("2020-01-01"),
                "FAKE_USER",
                "2020-01-01", "EUR", "CAD",
                15.3, 17.3, "decreasing",
                convertDateString("2020-01-01")
        )
        exchangeRateRepository.save(exchangeRateQuery2)
    }

    private fun prepareDatabaseWithOtherQueries() {
        val exchangeRateQuery1 = ExchangeRateQuery(
                0,
                convertDateString("2019-12-31"),
                "COUNT_USER",
                "2019-12-31", "USD", "CAD",
                12.3, 13.2, "decreasing",
                convertDateString("2019-12-31")
        )
        exchangeRateRepository.save(exchangeRateQuery1)
        val exchangeRateQuery2 = ExchangeRateQuery(
                0,
                convertDateString("2020-01-01"),
                "COUNT_USER",
                "2020-01-01", "EUR", "CAD",
                15.3, 17.3, "decreasing",
                convertDateString("2020-01-01")
        )
        exchangeRateRepository.save(exchangeRateQuery2)
        val exchangeRateQuery3 = ExchangeRateQuery(
                0,
                convertDateString("2020-01-01"),
                "COUNT_USER",
                "2020-01-01", "CHF", "EUR",
                0.83, 0.95, "ascending",
                convertDateString("2020-01-01")
        )
        exchangeRateRepository.save(exchangeRateQuery3)
    }

}