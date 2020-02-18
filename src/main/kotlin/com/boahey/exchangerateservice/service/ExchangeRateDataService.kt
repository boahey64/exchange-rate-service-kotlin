package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.model.ExchangeRateQuery
import com.boahey.exchangerateservice.repository.ExchangeRateRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class ExchangeRateDataService(private val exchangeRateRepository: ExchangeRateRepository) {

    @Transactional
    fun add(exchangeRateQuery: ExchangeRateQuery): ExchangeRateQuery? {
        return exchangeRateRepository.save<ExchangeRateQuery?>(exchangeRateQuery)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<ExchangeRateQuery?>? {
        return exchangeRateRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findByStartDateBetween(start: Date?, end: Date?): List<ExchangeRateQuery?>? {
        return exchangeRateRepository.findByStartDateBetween(start!!, end!!)
    }

    @Transactional(readOnly = true)
    fun countByCustomerId(customerId: String?): Long? {
        return exchangeRateRepository.countByCustomerId(customerId!!)
    }


    @Transactional(readOnly = true)
    fun countByCustomerIdAndQueryDateBetween(customerId: String?, start: Date?, end: Date?): Long? {
        return exchangeRateRepository.countByCustomerIdAndQueryDateBetween(customerId!!, start!!, end!!)
    }

}