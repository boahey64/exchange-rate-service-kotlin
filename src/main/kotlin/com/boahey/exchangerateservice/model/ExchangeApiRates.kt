package com.boahey.exchangerateservice.model

data class ExchangeApiRates (
    private val rates: Map<String, Double>?,
    private val base: String,
    private val date: String
)