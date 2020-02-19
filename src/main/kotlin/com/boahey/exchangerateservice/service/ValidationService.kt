package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.exception.ExceededAllowedQueriesException
import com.boahey.exchangerateservice.exception.InvalidCurrencyException
import com.boahey.exchangerateservice.exception.InvalidDateException
import com.boahey.exchangerateservice.model.ExchangeRateValidationInput
import com.boahey.exchangerateservice.util.convertDateString
import com.boahey.exchangerateservice.util.getZeroOClockDate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class ValidationService {

    fun checkExchangeRateInput(validationInput: ExchangeRateValidationInput): Boolean {
        if (!isValidCurrency(validationInput.validCurrencies,
                        validationInput.baseCurrency)) {
            throw InvalidCurrencyException("Base currency " + validationInput.baseCurrency.toString() + " is not valid")
        }
        if (!isValidCurrency(validationInput.validCurrencies, validationInput.targetCurrency)) {
            throw InvalidCurrencyException("Target currency " + validationInput.targetCurrency.toString() + " is not valid")
        }
        if (!isValidDate(validationInput.date)) {
            throw InvalidDateException("Date is out of range: " + validationInput.date)
        }
        if (validationInput.monthlyUsageInfo.currentUsage > validationInput.monthlyUsageInfo.maxUsage) {
            throw ExceededAllowedQueriesException(("Exceeded monthly allowed queries "
                    + validationInput.monthlyUsageInfo.maxUsage
                    ) + " for: " + validationInput.customerId)
        }
        if (validationInput.yearlyUsageInfo.currentUsage > validationInput.yearlyUsageInfo.maxUsage) {
            throw ExceededAllowedQueriesException(("Exceeded yearly allowed queries "
                    + validationInput.yearlyUsageInfo.maxUsage
                    ) + " for: " + validationInput.customerId)
        }
        return true
    }

    fun checkDateInput(dateString: String): Boolean {
        if (!isValidDate(dateString)) {
            throw InvalidDateException("Date is out of range: $dateString")
        }
        return true
    }

    private fun isValidCurrency(validCurrencies: Set<*>, currency: String): Boolean {
        return validCurrencies.contains(currency)
    }

    private fun isValidDate(dateString: String): Boolean {
        val date: Date = convertDateString(dateString)
        val startDate: Date = convertDateString("2000-01-01")
        val yesterdayMidnight: Date = getZeroOClockDate(LocalDate.now())
        return date.time > startDate.time && date.time < yesterdayMidnight.time
    }

}