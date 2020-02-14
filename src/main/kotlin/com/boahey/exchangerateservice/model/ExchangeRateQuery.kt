package com.boahey.exchangerateservice.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ExchangeRateQuery(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long,
    private val startDate: Date,
    private val customerId: String,
    private val dateString: String,
    private val baseCurrency: String,
    private val targetCurrency: String,
    private val currentRate: Double,
    private val averageRate: Double,
    private val trend: String,
    private val queryDate: Date
)