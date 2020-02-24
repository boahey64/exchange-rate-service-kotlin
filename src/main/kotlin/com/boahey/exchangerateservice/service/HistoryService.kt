package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeApiRatesHistory
import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.model.HistoryDate
import com.boahey.exchangerateservice.util.convertDateToDateString
import com.boahey.exchangerateservice.util.getZeroOClockDateFromHistoryDate
import com.boahey.exchangerateservice.util.getZeroOClockDateOfFirstDayInNextMonthFromHistoryDate
import com.boahey.exchangerateservice.util.normalizeMonthlyHistoryDate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class HistoryService(
        private val exchangeRateDataService: ExchangeRateDataService,
        private val exchangeRateService: ExchangeRateService,
        private val validationService: ValidationService
) {
    private val log = LoggerFactory.getLogger(HistoryService::class.java)

    fun getDailyExchangeRateQueriesLocal(historyDate: HistoryDate): List<ExchangeRateQuery?>? {
        validationService.checkDateInput(getDateString(historyDate))
        log.debug("daily: {}", historyDate)
        return findLocalDailyExchangeRateQueries(historyDate)
    }

    fun getMonthlyExchangeRateQueriesLocal(historyDate: HistoryDate?): List<ExchangeRateQuery?>? {
        validationService.checkDateInput(getDateString(historyDate!!))
        log.debug("monthly: {}", historyDate)
        return findLocalMonthlyExchangeRateQueries(historyDate)
    }

    fun getDailyExchangeApiRates(historyDate: HistoryDate?): ExchangeApiRatesHistory? {
        validationService.checkDateInput(getDateString(historyDate!!))
        log.debug("daily: {}", historyDate)
        return findDailyExchangeRateQueries(historyDate)
    }

    fun getMonthlyExchangeApiRates(historyDate: HistoryDate?): ExchangeApiRatesHistory? {
        validationService.checkDateInput(getDateString(historyDate!!))
        log.debug("monthly: {}", historyDate)
        return findMonthlyExchangeRateQueries(historyDate)
    }

    private fun getDateString(historyDate: HistoryDate): String {
        if (historyDate.dd == null) {
            val zeroOClockOfHistoryDate: Date = getZeroOClockDateFromHistoryDate(
                    normalizeMonthlyHistoryDate(historyDate),
                    0)
            return convertDateToDateString(zeroOClockOfHistoryDate)
        }
        val historyDateDate: Date = getZeroOClockDateFromHistoryDate(historyDate, 0)
        return convertDateToDateString(historyDateDate)
    }

    private fun findDailyExchangeRateQueries(historyDate: HistoryDate): ExchangeApiRatesHistory? {
        val dailyRange = findDailyStartAndEnd(historyDate)
        return exchangeRateService.getExchangeRateHistory(
                convertDateToDateString(dailyRange["start"]),
                convertDateToDateString(dailyRange["end"])
        )
    }

    private fun findMonthlyExchangeRateQueries(historyDate: HistoryDate): ExchangeApiRatesHistory? {
        val monthlyRange = findMonthlyStartAndEnd(historyDate)
        return exchangeRateService.getExchangeRateHistory(
                convertDateToDateString(monthlyRange["start"]),
                convertDateToDateString(monthlyRange["end"])
        )
    }

    private fun findLocalDailyExchangeRateQueries(historyDate: HistoryDate): List<ExchangeRateQuery?>? {
        val dailyRange = findDailyStartAndEnd(historyDate)
        return exchangeRateDataService.findByStartDateBetween(dailyRange["start"], dailyRange["end"])
    }

    private fun findLocalMonthlyExchangeRateQueries(historyDate: HistoryDate): List<ExchangeRateQuery?>? {
        val monthlyRange = findMonthlyStartAndEnd(historyDate)
        return exchangeRateDataService.findByStartDateBetween(monthlyRange["start"], monthlyRange["end"])
    }

    private fun findMonthlyStartAndEnd(historyDate: HistoryDate): Map<String, Date> {
        val normalizedHistoryDate: HistoryDate = normalizeMonthlyHistoryDate(historyDate)
        val startOfMonth: Date = getZeroOClockDateFromHistoryDate(normalizedHistoryDate, 0)
        log.info("start: {} -> {}", normalizedHistoryDate, startOfMonth)
        val endOfMonth: Date = getZeroOClockDateOfFirstDayInNextMonthFromHistoryDate(normalizedHistoryDate)
        log.info("end: {} -> {}", normalizedHistoryDate, endOfMonth)
        val range: MutableMap<String, Date> = HashMap()
        range["start"] = startOfMonth
        range["end"] = endOfMonth
        return range
    }


    private fun findDailyStartAndEnd(historyDate: HistoryDate): Map<String, Date> {
        val startOfDay: Date = getZeroOClockDateFromHistoryDate(historyDate, 0)
        log.debug("start: {} -> {}", historyDate, startOfDay)
        val endOfDay: Date = getZeroOClockDateFromHistoryDate(historyDate, 1)
        log.debug("end: {} -> {}", historyDate, endOfDay)
        val range: MutableMap<String, Date> = HashMap()
        range["start"] = startOfDay
        range["end"] = endOfDay
        return range
    }

}