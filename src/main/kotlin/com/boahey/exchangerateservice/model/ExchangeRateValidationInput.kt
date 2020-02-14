package com.boahey.exchangerateservice.model

import com.boahey.exchangerateservice.account.model.UsageInfo

data class ExchangeRateValidationInput(
        private var date: String?,
        private val baseCurrency: String,
        private val targetCurrency: String,
        private val validCurrencies: Set<*>,
        private val customerId: String,
        private val monthlyUsageInfo: UsageInfo,
        private val yearlyUsageInfo: UsageInfo

)