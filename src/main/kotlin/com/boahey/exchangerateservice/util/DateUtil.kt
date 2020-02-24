package com.boahey.exchangerateservice.util

import com.boahey.exchangerateservice.exception.InvalidDateFormatException
import com.boahey.exchangerateservice.model.HistoryDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.concurrent.ThreadLocalRandom


fun convertDateString(date: String): Date {
    return try {
        SimpleDateFormat("yyyy-MM-dd").parse(date)
    } catch (e: ParseException) {
        throw InvalidDateFormatException("Invalid date format: $date", e)
    }
}

fun convertDateString(historyDate: HistoryDate): Date {
    val date: String = historyDate.yyyy + "-" + historyDate.MM + "-" + historyDate.dd
    return convertDateString(date)
}

fun getZeroOClockDate(date: LocalDate): Date {
    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun getZeroOClockDateFromHistoryDate(historyDate: HistoryDate, addedDays: Int): Date {
    val localDate = convertHistoryDateToLocalDate(historyDate).plusDays(addedDays.toLong())
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun getZeroOClockDateOfFirstDayInNextMonthFromHistoryDate(historyDate: HistoryDate): Date {
    val firstDayOfNextMonth = lastDayOfMonth(historyDate).plusDays(1)
    return Date.from(firstDayOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun getZeroOClockDateOfFirstDayInNextYearFromHistoryDate(historyDate: HistoryDate): Date {
    val firstDayOfNextYear = lastDayOfYear(historyDate).plusDays(1)
    return Date.from(firstDayOfNextYear.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun convertDateStringToLocalDate(dateString: String): LocalDate {
    val tokens = dateString.split("-").toTypedArray()
    return getLocalDateFromDateTokens(tokens)
}

fun convertDateToDateString(date: Date?): String {
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return simpleDateFormat.format(date)
}

private fun getLocalDateFromDateTokens(tokens: Array<String>): LocalDate {
    // TODO eleminate !!
    return LocalDate.of(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
}

fun convertHistoryDateToLocalDate(historyDate: HistoryDate): LocalDate {
    val tokens = arrayOf<String>(historyDate.yyyy, historyDate.MM, historyDate.dd)
    return getLocalDateFromDateTokens(tokens)
}

private fun lastDayOfMonth(historyDate: HistoryDate): LocalDate {
    val tokens = arrayOf(historyDate.yyyy, historyDate.MM, historyDate.dd)
    val dayOfMonth = getLocalDateFromDateTokens(tokens)
    return dayOfMonth.with(TemporalAdjusters.lastDayOfMonth())
}

private fun lastDayOfYear(historyDate: HistoryDate): LocalDate {
    val tokens = arrayOf(historyDate.yyyy, historyDate.MM, historyDate.dd)
    val dayOfYear = getLocalDateFromDateTokens(tokens)
    return dayOfYear.with(TemporalAdjusters.lastDayOfYear())
}

fun normalizeMonthlyHistoryDate(historyDate: HistoryDate): HistoryDate {
    return HistoryDate(historyDate.yyyy, historyDate.MM, "01")
}

fun normalizeYearlyHistoryDate(historyDate: HistoryDate): HistoryDate {
    return HistoryDate(historyDate.yyyy, "01", "01")
}

fun currentDate(): Date? {
    return Date()
}

fun getCurrentDateString(): HistoryDate {
    val localDate = LocalDate.now()
    return calculateDateStringFromLocalDate(localDate)
}

fun getRandomDateString(): HistoryDate {
    val start = LocalDate.of(2000, Month.JANUARY, 1)
    val end = LocalDate.now()
    val localDate: LocalDate? = between(start, end)
    return calculateDateStringFromLocalDate(localDate)
}

private fun calculateDateStringFromLocalDate(localDate: LocalDate?): HistoryDate {
    val yearValue = localDate?.format(DateTimeFormatter.ofPattern("yyyy")) ?: "2000"
    val monthValue = localDate?.format(DateTimeFormatter.ofPattern("MM")) ?: "01"
    val dayValue = localDate?.format(DateTimeFormatter.ofPattern("dd")) ?: "01"
    return HistoryDate(yearValue, monthValue, dayValue)
}

fun between(startInclusive: LocalDate, endExclusive: LocalDate): LocalDate? {
    val startEpochDay = startInclusive.toEpochDay()
    val endEpochDay = endExclusive.toEpochDay()
    val randomDay = ThreadLocalRandom
            .current()
            .nextLong(startEpochDay, endEpochDay)
    return LocalDate.ofEpochDay(randomDay)
}
