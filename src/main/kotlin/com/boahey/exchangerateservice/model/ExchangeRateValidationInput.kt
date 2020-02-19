package com.boahey.exchangerateservice.model

import com.boahey.exchangerateservice.account.model.UsageInfo

data class ExchangeRateValidationInput(
        val date: String,
        val baseCurrency: String,
        val targetCurrency: String,
        val validCurrencies: Set<*>,
        val customerId: String,
        val monthlyUsageInfo: UsageInfo,
        val yearlyUsageInfo: UsageInfo

)