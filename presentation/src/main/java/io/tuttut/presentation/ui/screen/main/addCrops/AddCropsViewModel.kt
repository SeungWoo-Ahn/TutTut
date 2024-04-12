package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.constant.CUSTOM_KEY
import io.tuttut.data.constant.CUSTOM_NAME
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCropsViewModel @Inject constructor(
    private val cropsRepo: CropsRepository,
    val cropsInfoRepo: CropsInfoRepository,
    private val prefs: PreferenceUtil,
    private val cropsModel: CropsModel,
): BaseViewModel() {

    private val crops = cropsModel.selectedCrops.value
    val totalCrops = listOf(CropsInfo()) + cropsInfoRepo.cropsInfoList.value

    private val _uiState = MutableStateFlow<AddCropsUiState>(AddCropsUiState.Nothing)
    val uiState: StateFlow<AddCropsUiState> = _uiState

    var editMode by mutableStateOf(cropsModel.editMode.value)
    var showSheet by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)

    private val _cropsType = mutableStateOf(crops.key)
    val cropsType: State<String> = _cropsType

    private val _customMode = mutableStateOf(crops.key == CUSTOM_KEY)
    val customMode: State<Boolean> = _customMode

    private val _plantingDate = mutableStateOf(crops.plantingDate)
    val plantingDate: State<String> = _plantingDate

    private val _typedCustomName = mutableStateOf(crops.name)
    val typedCustomName: State<String> = _typedCustomName

    private val _typedNickName = mutableStateOf(crops.nickName)
    val typedNickName: State<String> = _typedNickName

    private val _typedWateringInterval = mutableStateOf(crops.wateringInterval.convertDayString())
    val typedWateringInterval: State<String> = _typedWateringInterval
    private val _offWateringInterval = mutableStateOf(editMode && crops.wateringInterval == null)
    val offWateringInterval: State<Boolean> = _offWateringInterval

    private val _typedGrowingDay = mutableStateOf(crops.growingDay.convertDayString())
    val typedGrowingDay: State<String> = _typedGrowingDay
    private val _offGrowingDay = mutableStateOf(editMode && crops.growingDay == null)
    val offGrowingDay: State<Boolean> = _offGrowingDay

    private val _needAlarm = mutableStateOf(false)
    val needAlarm: State<Boolean> = _needAlarm

    fun onCropsType(cropsInfo: CropsInfo) {
        _cropsType.value = cropsInfo.key
        _customMode.value = cropsInfo.key == CUSTOM_KEY
        _typedWateringInterval.value = cropsInfo.wateringInterval.convertDayString()
        _offWateringInterval.value = false
        _offGrowingDay.value = false
        resetCustomName()
        resetGrowingDay()
    }

    fun onDateSelected(date: String) {
        _plantingDate.value = date
        showDatePicker = false
    }

    fun typeCustomName(text: String) {
        if (text.length <= 10) {
            _typedCustomName.value = text
        }
    }

    fun resetCustomName() {
        _typedCustomName.value = ""
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

    fun onOffWateringIntervalChanged(state: Boolean) {
        if (state) resetWateringInterval()
        _offWateringInterval.value = state
    }

    fun typeGrowingDay(text: String) {
        val filteredText = text.split(" ")[0]
        if (!filteredText.all { it in '0' .. '9' }) return
        if (filteredText.isEmpty()) {
            resetGrowingDay()
        } else if (filteredText.length <= 3) {
            _typedGrowingDay.value = "$filteredText 일"
        }
    }

    fun resetGrowingDay() {
        _typedGrowingDay.value = ""
    }

    fun onOffGrowingDayChanged(state: Boolean) {
        if (state) resetGrowingDay()
        _offGrowingDay.value = state
    }

    fun onAlarmSwitch(state: Boolean) {
        _needAlarm.value = state
    }

    fun onButton(moveBack: () -> Unit, moveCrops: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            if (editMode) editCrops(moveBack, onShowSnackBar)
            else addCrops(moveCrops, onShowSnackBar)
        }
    }

    private suspend fun addCrops(moveCrops: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val key = cropsType.value
        val cropsInfo = cropsInfoRepo.cropsInfoMap[key]
        val newCrops = Crops(
            key = key,
            name = if (customMode.value) typedCustomName.value.trim() else cropsInfo?.name ?: CUSTOM_NAME,
            nickName = typedNickName.value.trim(),
            lastWatered = getToday(),
            plantingDate = plantingDate.value,
            wateringInterval = if (offWateringInterval.value) null else typedWateringInterval.value.convertDayInt(),
            growingDay = if (customMode.value) {
                if (offGrowingDay.value) null
                else typedGrowingDay.value.convertDayInt()
            } else cropsInfo?.growingDay
        )
        cropsRepo.addCrops(prefs.gardenId, newCrops).collect {
            when (it) {
                is Result.Success -> {
                    cropsModel.refreshCropsList(newCrops)
                    cropsModel.observeCrops(it.data)
                    moveCrops()
                    onShowSnackBar("${newCrops.nickName}을/를 추가했어요", null)
                }
                Result.Loading -> _uiState.value = AddCropsUiState.Loading
                else -> { onShowSnackBar("작물 추가에 실패했어요", null) }
            }
            _uiState.value = AddCropsUiState.Nothing
        }
    }

    private suspend fun editCrops(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val updatedCrops = crops.copy(
            name = if (customMode.value) typedCustomName.value.trim() else crops.name,
            nickName = typedNickName.value.trim(),
            lastWatered = crops.lastWatered,
            plantingDate = plantingDate.value,
            wateringInterval = if (offWateringInterval.value) null else typedWateringInterval.value.convertDayInt(),
            growingDay = if (customMode.value) {
                if (offGrowingDay.value) null
                else typedGrowingDay.value.convertDayInt()
            } else crops.growingDay,
            needAlarm = crops.needAlarm
        )
        cropsRepo.updateCrops(prefs.gardenId, updatedCrops).collect {
            when (it) {
                is Result.Success -> {
                    cropsModel.refreshCropsList(updatedCrops)
                    moveBack()
                    onShowSnackBar("${updatedCrops.nickName}을/를 수정했어요", null)
                }
                Result.Loading -> _uiState.value = AddCropsUiState.Loading
                else -> { onShowSnackBar("작물 수정에 실패했어요", null) }
            }
            _uiState.value = AddCropsUiState.Nothing
        }
    }

    private fun Int?.convertDayString(): String = this?.toString()?.let { "$it 일" } ?: ""

    private fun String.convertDayInt(): Int = replace("일", "").trim().toInt()
}