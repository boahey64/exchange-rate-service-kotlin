package com.boahey.exchangerateservice.service

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class ValidDaysServiceTest {
    private val serviceUnderTest: ValidDaysService = ValidDaysService()

    @Test
    fun calculate_5_valid_days_before_2019_12_31() {
        val count = 5
        val actual: List<String> = serviceUnderTest.calculateNValidDaysBackFrom("2019-12-31", count)
        MatcherAssert.assertThat(actual, Matchers.contains("2019-12-30", "2019-12-27", "2019-12-26", "2019-12-25", "2019-12-24"))
        MatcherAssert.assertThat(actual.size, Matchers.`is`(count))
    }

}