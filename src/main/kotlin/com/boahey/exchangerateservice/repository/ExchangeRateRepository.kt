package com.boahey.exchangerateservice.repository

import com.boahey.exchangerateservice.model.ExchangeRateQuery
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ExchangeRateRepository: CrudRepository<ExchangeRateQuery, Long> {
    override fun findAll(): List<ExchangeRateQuery>
    fun findByStartDateBetween(start: Date, end: Date): List<ExchangeRateQuery>
    fun countByCustomerId(customerId: String): Long
    fun countByCustomerIdAndQueryDateBetween(customerId: String, start: Date, end: Date): Long
}