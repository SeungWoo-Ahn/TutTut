package io.tuttut.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed interface DateFormatStrategy {
    fun format(): String

    data class DDay(
        private val date: String,
        private val interval: Int
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDifference = calcDaysDifference()
            return when {
                daysDifference == 0L -> "D-DAY"
                daysDifference > 0 -> "D + $daysDifference"
                else -> "D - $daysDifference"
            }
        }

        private fun calcDaysDifference(): Long {
            val current = Date()
            val target = formatter.parse(date)
            val targetTime = Calendar
                .getInstance()
                .apply {
                    time = target as Date
                    add(Calendar.DAY_OF_YEAR, interval)
                }
                .time
            return (targetTime.time - current.time) / (1000 * 60 * 60 * 24)
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val formatter = SimpleDateFormat(DATE_PATTERN, Locale.KOREA)
    }
}

fun format(dateFormatStrategy: DateFormatStrategy): String = dateFormatStrategy.format()