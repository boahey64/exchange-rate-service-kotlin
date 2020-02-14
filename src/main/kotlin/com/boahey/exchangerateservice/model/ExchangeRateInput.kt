package com.boahey.exchangerateservice.model

data class ExchangeRateInput(
    private var date: String,
    private val baseCurrency: String,
    private val targetCurrency: String

)