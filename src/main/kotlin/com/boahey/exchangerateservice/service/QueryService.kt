package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRate
import com.boahey.exchangerateservice.model.ExchangeRateInput
import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.util.convertDateString
import org.springframework.stereotype.Component
import java.util.*

@Component
class QueryService(private val exchangeRateDataService: ExchangeRateDataService) {
    fun saveQuery(exchangeRateInput: ExchangeRateInput, customerId: String, exchangeRate: ExchangeRate, queryDate: Date): ExchangeRateQuery? {
        val exchangeRateQuery = ExchangeRateQuery(
                0,
                convertDateString(exchangeRateInput.date),
                customerId,
                exchangeRateInput.date,
                exchangeRateInput.baseCurrency,
                exchangeRateInput.targetCurrency,
                exchangeRate.currentRate,
                exchangeRate.averageRate,
                exchangeRate.trend,
                queryDate
                )
        return exchangeRateDataService.add(exchangeRateQuery)
    }

}