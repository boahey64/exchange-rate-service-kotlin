package com.boahey.exchangerateservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CustomGlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(InvalidDateException::class)
    fun customHandleInvalidDate(ex: Exception, request: WebRequest?): ResponseEntity<CustomErrorResponse?>? {
        val errors = CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.message ?: "",
                "The date has to be between 2000-01-01 and yesterday."
        )
        return ResponseEntity(errors, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidDateFormatException::class)
    fun customHandleInvalidDateFormat(ex: Exception, request: WebRequest?): ResponseEntity<CustomErrorResponse?>? {
        val errors = CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.message ?: "",
                "Use the correct date format: yyyy-MM-dd."
        )
        return ResponseEntity(errors, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidCurrencyException::class)
    fun customHandleInvalidCurrency(ex: Exception, request: WebRequest?): ResponseEntity<CustomErrorResponse?>? {
        val errors = CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.message ?: "",
                "You try to fetch the exchange rate for a not supported currency."
        )
        return ResponseEntity(errors, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ExceededAllowedQueriesException::class)
    fun customHandleExceededAllowedQueries(ex: Exception, request: WebRequest?): ResponseEntity<CustomErrorResponse?>? {
        val errors = CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.message ?: "",
                "The customer reaches the maximal query quota. " +
                        "Customers with FREE account can query 10 times a day and 1000 times a year." +
                        "Customers with PREMIUM account can query 1000 times a day ann 100000 times a year."
        )
        return ResponseEntity(errors, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NotExpectedErrorException::class)
    fun customHandleNotExpectedError(ex: Exception?, request: WebRequest?): ResponseEntity<CustomErrorResponse?>? {
        val errors = CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not expected error.",
                "Not expected error."
        )
        return ResponseEntity(errors, HttpStatus.NOT_FOUND)
    }

}