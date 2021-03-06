package com.boahey.exchangerateservice.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CustomErrorResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    val timestamp: LocalDateTime,
    val status: Int,
    val title: String,
    val description: String
)
