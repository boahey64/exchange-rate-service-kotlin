package com.boahey.exchangerateservice.model

enum class ExchangeRateTrend(val label: String) {
    DESCENDING("descending"),
    ASCENDING("ascending"),
    CONSTANT("constant"),
    UNDEFINED("undefined");
}