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
                daysDiff > 0L -> "D - $daysDiff"
                else -> "D + ${-daysDiff}"
            }
        }
    }

    data class PlantingDay(
        private val date: String
    ) : DateFormatStrategy {
        override fun format(): String {
            val daysDiff = -calcDaysDifference(date, 0)
            return when {
                daysDiff >= 0L -> "${daysDiff + 1}일"
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
                daysDiff > 0L -> "${daysDiff}일 후"
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
                daysDiff > 0L -> "${daysDiff}일 후"
                else -> "${-daysDiff}일 지남"
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

    data class RelativeTime(
        private val date: String
    ) : DateFormatStrategy {
        override fun format(): String {
            val parsedDate = formatter.parse(date)
            val current = Date()
            val diff = current.time - (parsedDate as Date).time

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
    }

    data class FullDate(
        val date: String,
    ) : DateFormatStrategy {
        override fun format(): String {
            val (yyyy, mm, dd) = date.split("-")
            return "${yyyy}년 ${mm}월 ${dd}일"
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val formatter = SimpleDateFormat(DATE_PATTERN, Locale.KOREA)
    }
}

fun format(dateFormatStrategy: DateFormatStrategy): String = dateFormatStrategy.format()