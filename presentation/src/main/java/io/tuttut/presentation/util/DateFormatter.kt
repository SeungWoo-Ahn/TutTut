package io.tuttut.presentation.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

fun getDDay(lastDate: String, interval: Int): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val currentDate = Date()

    val lastWateredDate = dateFormat.parse(lastDate)
    val calendar = Calendar.getInstance().apply {
        time = lastWateredDate as Date
        add(Calendar.DAY_OF_YEAR, interval)
    }

    val nextWateringDate = calendar.time
    return ((nextWateringDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()
}

fun getDDayStr(lastDate: String, interval: Int): String {
    val daysDifference = getDDay(lastDate, interval)
    if (daysDifference == 0) return "D-DAY"
    val prefix = if (daysDifference < 0) "+" else "-"
    return "D $prefix ${daysDifference.absoluteValue}"
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    return formatter.format(Date(millis))
}

fun getToday(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val today = Calendar.getInstance().time
    return dateFormat.format(today)
}

fun getFormattedDate(date: String): String {
    val splitDate = if (date.isEmpty()) getToday().split("-") else date.split("-")
    return "${splitDate[0]}년 ${splitDate[1]}월 ${splitDate[2]}일"
}

fun getDatePickerYearRange(): IntRange {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val nextYear = currentYear + 1
    return currentYear..nextYear
}

fun getCurrentMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH) + 1
}