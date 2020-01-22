package com.boahey.exchangerateservicekotlin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/exchange-rate")
class ExchangeRateController {

    @GetMapping("/")
    fun getExchangeRate(): String {
        return "Hello ExchangeRate Controller"
    }


}