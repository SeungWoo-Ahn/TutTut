package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CUSTOM_KEY
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.util.getToday
import javax.inject.Inject

@HiltViewModel
class AddCropsViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    cropsModel: CropsModel,
): BaseViewModel() {

    var editMode by mutableStateOf(cropsModel.editMode.value)
    var showSheet by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)

    private val _cropsType = mutableStateOf(cropsModel.selectedCropsInfo.value)
    val cropsType: State<CropsInfo> = _cropsType

    var customMode by mutableStateOf(cropsType.value.key == CUSTOM_KEY)

    private val _plantingDate = mutableStateOf(getToday())
    val plantingDate: State<String> = _plantingDate

    private val _typedNickName = mutableStateOf("")
    val typedNickName: State<String> = _typedNickName

    private val _typedWateringInterval = mutableStateOf(cropsType.value.wateringInterval?.toString()?.let { "$it 일" } ?: "")
    val typedWateringInterval: State<String> = _typedWateringInterval

    private val _needAlarm = mutableStateOf(false)
    val needAlarm: State<Boolean> = _needAlarm

    fun onCropsType(cropsInfo: CropsInfo) {
        _cropsType.value = cropsInfo
        _typedWateringInterval.value = cropsInfo.wateringInterval?.toString()?.let { "$it 일" } ?: ""
        showSheet = false
    }

    fun onDateSelected(date: String) {
        _plantingDate.value = date
        showDatePicker = false
    }

    fun typeNickName(text: String) {
        if (text.length <= 10) {
            _typedNickName.value = text
        }
    }

    fun resetNickName() {
        _typedNickName.value = ""
    }

    fun typeWateringInterval(text: String) {
        val filteredText = text.split(" ")[0]
        if (!filteredText.all { it in '0' .. '9' }) return
        if (filteredText.isEmpty()) {
            resetWateringInterval()
        } else if (filteredText.length <= 3) {
            _typedWateringInterval.value = "$filteredText 일"
        }
    }

    fun resetWateringInterval() {
        _typedWateringInterval.value = ""
    }

    fun onAlarmSwitch(state: Boolean) {
        _needAlarm.value = state
    }
}