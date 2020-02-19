package com.boahey.exchangerateservice.service

import org.springframework.stereotype.Component

@Component
class HistoryService(
        private val exchangeRateDataService: ExchangeRateDataService
) {
}