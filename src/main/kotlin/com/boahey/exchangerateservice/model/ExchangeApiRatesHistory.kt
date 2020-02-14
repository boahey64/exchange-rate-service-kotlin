package com.boahey.exchangerateservice.model

data class ExchangeApiRatesHistory(
        private var rates: Map<String?, Map<String?, Double?>?>,
        private val start_at: String ,
        private val base: String,
        private val end_at: String

)