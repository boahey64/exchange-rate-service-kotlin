package com.boahey.exchangerateservice.acceptancetests

import com.nhaarman.mockito_kotlin.*

import com.boahey.exchangerateservice.acceptancetests.config.ATBase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.timeout
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.jupiter.api.Test


class ExchangeRateIT : ATBase() {

    @Test
    fun `open landing page`() {
//        givenSomething()

        navigateToExchangeRateRate()
//
//        el("[data-e2e-heart]").click()

//        verify(exchangeRateClient, timeout(2000)).someaction(any(), any(), any())

//        assertThat(url()).endsWith("/exchange-rate/rate")
    }

}