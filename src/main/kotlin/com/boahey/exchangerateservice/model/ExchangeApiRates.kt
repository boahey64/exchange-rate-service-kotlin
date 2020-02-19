package com.boahey.exchangerateservice.model

data class ExchangeApiRates (
    val rates: Map<String, Double>,
    val base: String,
    val date: String
)