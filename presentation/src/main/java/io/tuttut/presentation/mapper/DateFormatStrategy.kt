package io.tuttut.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed interface DateFormatStrategy {
    fun format(): String

    fun calcDaysDifference(date: String, interval: Int): Long {
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

    data class DDay(
        private val date: String,
        private val interval: Int
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = calcDaysDifference(date, interval)
            return when {
                daysDiff == 0L -> "D-DAY"
                daysDiff > 0L -> "D + $daysDiff"
                else -> "D - $daysDiff"
            }
        }
    }

    data class PlantingDay(
        private val date: String
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = calcDaysDifference(date, 0)
            return when {
                daysDiff == 0L -> "오늘"
                daysDiff > 0L -> "${daysDiff + 1}일"
                else -> "재배 전"
            }
        }
    }

    data class WateringDay(
        private val date: String,
        private val interval: Int
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = calcDaysDifference(date, interval)
            return when {
                daysDiff < 0L -> "${daysDiff}일 후"
                else -> "오늘"
            }
        }
    }

    data class HarvestDay(
        private val date: String,
        private val interval: Int,
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = calcDaysDifference(date, interval)
            return when {
                daysDiff == 0L -> "오늘"
                daysDiff > 0L -> "${daysDiff}일 지남"
                else -> "${-daysDiff}일 후"
            }
        }
    }

    data class LastWateredDay(
        private val date: String
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = calcDaysDifference(date, 0)
            return when {
                daysDiff == 0L -> "오늘"
                else -> "${daysDiff}일 전"
            }
        }
    }

    data object Today : DateFormatStrategy {
        override fun format(): String {
            val current = Date()
            return formatter.format(current)
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val formatter = SimpleDateFormat(DATE_PATTERN, Locale.KOREA)
    }
}

fun format(dateFormatStrategy: DateFormatStrategy): String = dateFormatStrategy.format()