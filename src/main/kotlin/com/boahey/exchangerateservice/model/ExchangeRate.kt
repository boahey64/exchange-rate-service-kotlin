package com.boahey.exchangerateservice.model

data class ExchangeRate(
    val currentRate: Double,
    val averageRate: Double,
    val trend: String
)