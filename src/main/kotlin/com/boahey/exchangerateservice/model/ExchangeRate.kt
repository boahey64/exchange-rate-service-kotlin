package com.boahey.exchangerateservice.model

data class ExchangeRate(
    private var currentRate: Double,
    private val averageRate: Double,
    private val trend: String
)