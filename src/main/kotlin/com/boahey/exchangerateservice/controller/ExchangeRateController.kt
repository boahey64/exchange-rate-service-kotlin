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

    @GetMapping("/currencies/valid")
    fun geValidCurrencies(): List<*> {
        return exchangeRateService.getValidCurrencies().toList()
    }

    @GetMapping("/history/local/daily/{yyyy}/{MM}/{dd}")
    fun getDailyHistoryLocal(
            @PathVariable yyyy: String,
            @PathVariable MM: String,
            @PathVariable dd: String
    ): List<ExchangeRateQuery?>? {
        val historyDate = HistoryDate(yyyy, MM, dd)
        return historyService.getDailyExchangeRateQueriesLocal(historyDate)
    }

    @GetMapping("/history/local/monthly/{yyyy}/{MM}")
    fun getMonthlyHistoryLocal(
            @PathVariable yyyy: String,
            @PathVariable MM: String
    ): List<ExchangeRateQuery?>? {
        val historyDate = HistoryDate(yyyy, MM)
        return historyService.getMonthlyExchangeRateQueriesLocal(historyDate)
    }

    @GetMapping("/history/daily/{yyyy}/{MM}/{dd}")
    fun getDailyHistory(
            @PathVariable yyyy: String,
            @PathVariable MM: String,
            @PathVariable dd: String
    ): ExchangeApiRatesHistory? {
        val historyDate = HistoryDate(yyyy, MM, dd)
        return historyService.getDailyExchangeApiRates(historyDate)
    }

    @GetMapping("/history/monthly/{yyyy}/{MM}")
    fun getMonthlyHistory(
            @PathVariable yyyy: String,
            @PathVariable MM: String
    ): ExchangeApiRatesHistory? {
        val historyDate = HistoryDate(yyyy, MM)
        return historyService.getMonthlyExchangeApiRates(historyDate)
    }


}