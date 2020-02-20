package com.boahey.exchangerateservice.account.service

import com.boahey.exchangerateservice.account.model.AccountInfo
import com.boahey.exchangerateservice.account.model.AccountType
import com.boahey.exchangerateservice.account.model.Period
import com.boahey.exchangerateservice.account.model.UsageInfo
import com.boahey.exchangerateservice.model.HistoryDate
import com.boahey.exchangerateservice.service.ExchangeRateDataService
import com.boahey.exchangerateservice.util.*
import java.util.*

const val FREE_MAX_ALLOWED_QUERIES_PER_MONTH = 4L
const val FREE_MAX_ALLOWED_QUERIES_PER_YEAR = 6L

const val PREMIUM_MAX_ALLOWED_QUERIES_PER_MONTH = 40L
const val PREMIUM_MAX_ALLOWED_QUERIES_PER_YEAR = 60L

class UsageService(
        private val exchangeRateDataService: ExchangeRateDataService,
        private val accountApiClient: AccountApiClient
) {

    fun getMonthlyUsageInfo(historyDate: HistoryDate, customerId: String): UsageInfo? {
        val currentQueryCount = getQueryCountForHistoryDate(historyDate, customerId, Period.MONTHLY)
        return UsageInfo(currentQueryCount, calculateAllowedQueryQuota(Period.MONTHLY, customerId))
    }

    fun getYearlyUsageInfo(historyDate: HistoryDate, customerId: String): UsageInfo? {
        val currentQueryCount = getQueryCountForHistoryDate(historyDate, customerId, Period.YEARLY)
        return UsageInfo(currentQueryCount, calculateAllowedQueryQuota(Period.YEARLY, customerId))
    }


    fun getQueryCountForHistoryDate(historyDate: HistoryDate, customerId: String, period: Period): Long {
        val normalizedHistoryDate: HistoryDate = getNormalizedHistoryDate(historyDate, period)
        val start: Date = getZeroOClockDateFromHistoryDate(normalizedHistoryDate, 0)
        val end = getZeroOClockDateOfFirstDayInNextMonthOrYearFromHistoryDate(normalizedHistoryDate, period)
        return exchangeRateDataService.countByCustomerIdAndQueryDateBetween(
                customerId, start, end)
    }

    private fun calculateAllowedQueryQuota(period: Period, customerId: String): Long {
        val accountInfo  = accountApiClient.getAccountInfo(customerId).block() ?: AccountInfo(customerId, AccountType.FREE)
        if (period.equals(Period.MONTHLY)) return if (accountInfo.type.equals(AccountType.FREE))
            FREE_MAX_ALLOWED_QUERIES_PER_MONTH
        else
            PREMIUM_MAX_ALLOWED_QUERIES_PER_MONTH
        return if (accountInfo.type.equals(AccountType.PREMIUM))
                    FREE_MAX_ALLOWED_QUERIES_PER_YEAR
                else
                    PREMIUM_MAX_ALLOWED_QUERIES_PER_YEAR
    }

    private fun getZeroOClockDateOfFirstDayInNextMonthOrYearFromHistoryDate(normalizedHistoryDate: HistoryDate, period: Period): Date {
        return if (period.equals(Period.MONTHLY))
                    getZeroOClockDateOfFirstDayInNextMonthFromHistoryDate(normalizedHistoryDate)
                else
                    getZeroOClockDateOfFirstDayInNextYearFromHistoryDate(normalizedHistoryDate)
    }

    private fun getNormalizedHistoryDate(historyDate: HistoryDate, period: Period): HistoryDate {
        val normalizedHistoryDate: HistoryDate
        normalizedHistoryDate = if (period.equals(Period.MONTHLY)) {
            normalizeMonthlyHistoryDate(historyDate)
        } else {
            normalizeYearlyHistoryDate(historyDate)
        }
        return normalizedHistoryDate
    }


}