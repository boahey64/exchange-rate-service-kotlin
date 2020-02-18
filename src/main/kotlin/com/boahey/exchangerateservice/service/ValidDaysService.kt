package com.boahey.exchangerateservice.service

import com.boahey.exchangerateservice.util.convertDateStringToLocalDate
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class ValidDaysService {

    fun calculateNValidDaysBackFrom(startDate: String, count: Int): List<String> {
        val dayList: MutableList<String> = ArrayList()
        var day = startDate
        for (i in 0 until count) {
            day = calculateValidDayBefore(day)
            dayList.add(day)
        }
        return dayList
    }


    private fun calculateValidDayBefore(dateString: String): String {
        var dayBefore: LocalDate = convertDateStringToLocalDate(dateString).minusDays(1)
        while (!isValidDay(dayBefore)) {
            dayBefore = dayBefore.minusDays(1)
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return dayBefore.format(formatter)
    }

    private fun isValidDay(localDate: LocalDate): Boolean {
        val dayOfWeek = DayOfWeek.from(localDate)
        return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
    }

}