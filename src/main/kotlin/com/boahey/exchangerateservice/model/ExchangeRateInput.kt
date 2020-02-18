package com.boahey.exchangerateservice.model

data class ExchangeRateInput(
    val date: String,
    val baseCurrency: String,
    val targetCurrency: String

)