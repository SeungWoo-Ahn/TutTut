package io.tuttut.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getDate(): String {
    val currentDate = Date()
    return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentDate)
}