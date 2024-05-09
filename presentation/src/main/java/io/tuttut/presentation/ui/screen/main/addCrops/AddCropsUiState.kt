package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.tuttut.data.constant.CUSTOM_NAME
import io.tuttut.presentation.ui.state.EditTextState

interface AddCropsUiState {
    data object Loading : AddCropsUiState
    data object Nothing : AddCropsUiState
}

class PlantingDateState(
    initDate: String
) {
    var selectedDate by mutableStateOf(initDate)
    var showDatePicker by mutableStateOf(false)

    fun showPicker() {
        showDatePicker = true
    }

    fun onDateSelected(date: String) {
        selectedDate = date
        showDatePicker = false
    }

    fun dismissPicker() {
        showDatePicker = false
    }
}

class CustomNameState(
    initText: String,
    maxLength: Int,
    private val customMode: Boolean
) : EditTextState(initText, maxLength) {
    fun nameByCustomMode(originName: String?): String {
        return if (customMode) typedText.trim()
        else originName ?: CUSTOM_NAME
    }

    override fun isValidate(): Boolean {
        return if (customMode) super.isValidate()
        else true
    }
}

class WateringIntervalState(
    initDate: Int?,
    private val maxLength: Int,
    editMode: Boolean
) : EditTextState(initDate.convertDayString(), maxLength) {
    var unUsed by mutableStateOf(editMode && initDate == null)

    override fun typeText(text: String) {
        val filteredText = text.split(" ").first()
        if (!filteredText.onlyDigit()) return
        if (filteredText.isEmpty()) resetText()
        else if (filteredText.length <= maxLength) {
            typedText = "$filteredText 일"
        }
    }

    fun changeType(interval: Int?) {
        typedText = interval.convertDayString()
        unUsed = false
    }

    fun changeUsedState(state: Boolean) {
        if (state) resetText()
        unUsed = state
    }

    fun intervalByUsed(): Int? {
        return if (unUsed) null
        else typedText.convertDayInt()
    }

    override fun isValidate(): Boolean {
        return if (!unUsed) super.isValidate()
        else true
    }
}

class GrowingDayState(
    initDate: Int?,
    private val maxLength: Int,
    editMode: Boolean,
    private val customMode: Boolean
) : EditTextState(initDate.convertDayString(), maxLength) {
    var unUsed by mutableStateOf(editMode && initDate == null)

    override fun typeText(text: String) {
        val filteredText = text.split(" ").first()
        if (!filteredText.onlyDigit()) return
        if (filteredText.isEmpty()) resetText()
        else if (filteredText.length <= maxLength) {
            typedText = "$filteredText 일"
        }
    }

    fun changeType(day: Int) {
        typedText = day.convertDayString()
        unUsed = false
    }

    fun changeUsedState(state: Boolean) {
        if (state) resetText()
        unUsed = state
    }

    fun dayByUsed(originDay: Int?): Int? {
        return if (customMode) {
            if (unUsed) null
            else typedText.convertDayInt()
        } else originDay
    }

    override fun isValidate(): Boolean {
        return if (!unUsed) super.isValidate()
        else true
    }
}

private fun Int?.convertDayString(): String = this?.toString()?.let { "$it 일" } ?: ""

private fun String.onlyDigit(): Boolean = all { it in '0' .. '9' }

private fun String.convertDayInt(): Int = replace("일", "").trim().toInt()
