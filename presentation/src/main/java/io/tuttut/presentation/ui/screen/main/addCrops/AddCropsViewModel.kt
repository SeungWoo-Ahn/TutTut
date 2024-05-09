package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.ui.screen.main.addCrops.AddCropsUiState.*
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.BottomSheetState
import io.tuttut.presentation.ui.state.EditTextState
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCropsViewModel @Inject constructor(
    private val cropsRepo: CropsRepository,
    val cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel,
    private val pref: PreferenceUtil
): BaseViewModel() {
    private val crops = cropsModel.observedCrops.value
    val totalCrops = listOf(CropsInfo()) + cropsInfoRepo.cropsInfoList.value

    var uiState by mutableStateOf<AddCropsUiState>(Nothing)
    var editMode by mutableStateOf(cropsModel.editMode.value)
    var customMode by mutableStateOf(crops.isCustom())
    var cropsType by mutableStateOf(crops.key)
    val typeSheetState = BottomSheetState()

    val plantingDateState = PlantingDateState(initDate = crops.plantingDate)
    val customNameState = CustomNameState(
        initText = crops.name,
        maxLength = 10,
        customMode = customMode
    )
    val nickNameState = EditTextState(
        initText = crops.nickName,
        maxLength = 10
    )
    val wateringIntervalState = WateringIntervalState(
        initDate = crops.wateringInterval,
        maxLength = 3,
        editMode = editMode
    )
    val growingDayState = GrowingDayState(
        initDate = crops.growingDay,
        maxLength = 3,
        editMode = editMode,
        customMode = customMode
    )
/*    val alarmState = CheckBoxState()*/

    fun onCropsType(cropsInfo: CropsInfo) {
        cropsInfo.run {
            cropsType = key
            customMode = isCustom()
            wateringIntervalState.changeType(wateringInterval)
            growingDayState.changeType(growingDay)
            customNameState.resetText()
        }
    }

    fun onButton(moveBack: () -> Unit, moveCrops: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            if (editMode) editCrops(moveBack, onShowSnackBar)
            else addCrops(moveCrops, onShowSnackBar)
        }
    }

    private suspend fun addCrops(moveCrops: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val key = cropsType
        val cropsInfo = cropsInfoRepo.cropsInfoMap[key]
        val newCrops = Crops(
            key = key,
            name = customNameState.nameByCustomMode(cropsInfo?.name),
            nickName = nickNameState.typedText.trim(),
            lastWatered = getToday(),
            plantingDate = plantingDateState.selectedDate,
            wateringInterval = wateringIntervalState.intervalByUsed(),
            growingDay = growingDayState.dayByUsed(cropsInfo?.growingDay)
        )
        cropsRepo.addCrops(pref.gardenId, newCrops).collect {
            when (it) {
                is Result.Success -> {
                    cropsModel.observeCrops(it.data)
                    moveCrops()
                    onShowSnackBar("${newCrops.nickName}을/를 추가했어요", null)
                }
                Result.Loading -> uiState = Loading
                else -> { onShowSnackBar("작물 추가에 실패했어요", null) }
            }
            uiState = Nothing
        }
    }

    private suspend fun editCrops(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val updatedCrops = crops.copy(
            name = customNameState.nameByCustomMode(crops.name),
            nickName = nickNameState.typedText.trim(),
            lastWatered = crops.lastWatered,
            plantingDate = plantingDateState.selectedDate,
            wateringInterval = wateringIntervalState.intervalByUsed(),
            growingDay = growingDayState.dayByUsed(crops.growingDay),
            needAlarm = crops.needAlarm
        )
        cropsRepo.updateCrops(pref.gardenId, updatedCrops).collect {
            when (it) {
                is Result.Success -> {
                    moveBack()
                    onShowSnackBar("${updatedCrops.nickName}을/를 수정했어요", null)
                }
                Result.Loading -> uiState = Loading
                else -> { onShowSnackBar("작물 수정에 실패했어요", null) }
            }
            uiState = Nothing
        }
    }
}