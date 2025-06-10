package io.tuttut.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateProvider {
    fun now(): String {
        val now = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        return formatter.format(now)
    }
}