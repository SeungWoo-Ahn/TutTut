package io.tuttut.presentation.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

fun getDDay(lastDate: String, gap: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val currentDate = Date()

    val lastWateredDate = dateFormat.parse(lastDate)
    val calendar = Calendar.getInstance().apply {
        time = lastWateredDate as Date
        add(Calendar.DAY_OF_YEAR, gap)
    }

    val nextWateringDate = calendar.time
    val daysDifference = ((nextWateringDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()
    if (daysDifference == 0) return "D-DAY"
    val prefix = if (daysDifference >= 0) "+" else "-"
    return "D$prefix${daysDifference.absoluteValue}"
}