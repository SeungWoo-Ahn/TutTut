package io.tuttut.presentation.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
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

fun getDateLong(date: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dateLong = dateFormat.parse(date) as Date
    return dateLong.time
}

fun getCurrentMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH) + 1
}

fun getRelativeTime(dateTime: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
    val date = formatter.parse(dateTime)
    val now = Date()

    val diff = now.time - (date as Date).time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = months / 12

    return when {
        seconds < 5 * 60 -> "방금"
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        days <= 30 -> "${days}일 전"
        months < 12 -> "${months}달 전"
        else -> "${years}년 전"
    }
}

fun getCurrentDate(): String {
    val currentDate = Date()
    return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentDate)
}

fun getCurrentDateTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
    return formatter.format(Date())
}