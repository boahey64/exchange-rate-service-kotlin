package com.boahey.exchangerateservice.controller

import com.boahey.exchangerateservice.account.service.UsageService
import com.boahey.exchangerateservice.exception.NotExpectedErrorException
import com.boahey.exchangerateservice.model.*
import com.boahey.exchangerateservice.service.ExchangeRateService
import com.boahey.exchangerateservice.service.HistoryService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/exchange-rate")
class ExchangeRateController(
        private val exchangeRateService: ExchangeRateService,
        private val historyService: HistoryService,
        private val usageService: UsageService
) {

    @GetMapping("/{date}/{baseCurrency}/{targetCurrency}")
    fun getExchangeRate(
            @PathVariable date: String,
            @PathVariable baseCurrency: String,
            @PathVariable targetCurrency: String,
            @RequestParam(defaultValue = "defaultUser") customerId: String?,
            @RequestParam(defaultValue = "false") randomQueryDate: Boolean?
    ): ExchangeRate? {

        val exchangeRateInput = ExchangeRateInput(date, baseCurrency, targetCurrency)
        val optionalExchangeRate: Optional<ExchangeRate>? = exchangeRateService.getExchangeData(
                exchangeRateInput, customerId!!, randomQueryDate!!)
        if (optionalExchangeRate!!.isPresent()) {
            return optionalExchangeRate.get()
        }
        throw NotExpectedErrorException()
    }

    @GetMapping("/history/local/daily/{yyyy}/{MM}/{dd}")
    fun getDailyHistoryLocal(historyDate: HistoryDate): List<ExchangeRateQuery?>? {
        return historyService.getDailyExchangeRateQueriesLocal(historyDate)
    }

    @GetMapping("/history/local/monthly/{yyyy}/{MM}")
    fun getMonthlyHistoryLocal(historyDate: HistoryDate?): List<ExchangeRateQuery?>? {
        return historyService.getMonthlyExchangeRateQueriesLocal(historyDate)
    }

    @GetMapping("/history/daily/{yyyy}/{MM}/{dd}")
    fun getDailyHistory(historyDate: HistoryDate?): ExchangeApiRatesHistory? {
        return historyService.getDailyExchangeApiRates(historyDate)
    }

    @GetMapping("/history/monthly/{yyyy}/{MM}")
    fun getMonthlyHistory(historyDate: HistoryDate?): ExchangeApiRatesHistory? {
        return historyService.getMonthlyExchangeApiRates(historyDate)
    }


}