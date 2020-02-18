package com.boahey.exchangerateservice.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ExchangeRateQuery(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val startDate: Date,
    val customerId: String,
    val dateString: String,
    val baseCurrency: String,
    val targetCurrency: String,
    val currentRate: Double,
    val averageRate: Double,
    val trend: String,
    val queryDate: Date
)