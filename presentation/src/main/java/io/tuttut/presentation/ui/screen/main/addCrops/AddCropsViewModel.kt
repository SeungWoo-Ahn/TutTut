package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.crops.AddCropsRequest
import io.tuttut.domain.model.crops.UpdateCropsRequest
import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.usecase.crops.AddCropsUseCase
import io.tuttut.domain.usecase.crops.GetCropsFlowUseCase
import io.tuttut.domain.usecase.crops.UpdateCropsUseCase
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoByKeyUseCase
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoListUseCase
import io.tuttut.domain.usecase.cropsInfo.GetRecommendedCropsInfoListUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.DateFormatStrategy
import io.tuttut.presentation.mapper.format
import io.tuttut.presentation.mapper.toItemUiModel
import io.tuttut.presentation.model.CropsInfoItemUiModel
import io.tuttut.presentation.navigation.MainScreen
import io.tuttut.presentation.ui.state.DayTextFieldState
import io.tuttut.presentation.ui.state.TextFieldState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCropsViewModel @Inject constructor(
    private val getCropsInfoByKeyUseCase: GetCropsInfoByKeyUseCase,
    private val getCropsFlowUseCase: GetCropsFlowUseCase,
    private val getCropsInfoListUseCase: GetCropsInfoListUseCase,
    private val getRecommendedCropsInfoListUseCase: GetRecommendedCropsInfoListUseCase,
    private val addCropsUseCase: AddCropsUseCase,
    private val updateCropsUseCase: UpdateCropsUseCase,
    savedStateHandle: SavedStateHandle,
): BaseViewModel() {
    private val route = savedStateHandle.toRoute<MainScreen.AddCrops>()
    val editMode = route.cropsId != null

    var uiState by mutableStateOf<AddCropsUiState>(AddCropsUiState.Idle)
        private set

    var cropsInfoItem by mutableStateOf<CropsInfoItemUiModel?>(null)
        private set

    var plantingDate by mutableStateOf(format(DateFormatStrategy.Today))
        private set

    val nameState = TextFieldState(10)
    val nickNameState = TextFieldState(10)
    val wateringIntervalState = DayTextFieldState()
    val growingDayState = DayTextFieldState()

    init {
        viewModelScope.launch {
            route.cropsKey?.let { key ->
                setCropsInfoData(key)
            }
            route.cropsId?.let { id ->
                setCropsData(id)
            }
        }
    }

    private suspend fun setCropsInfoData(cropsKey: CropsKey) {
        getCropsInfoByKeyUseCase(cropsKey)
            .getOrNull()
            ?.let { cropsInfo ->
                cropsInfoItem = cropsInfo.toItemUiModel()
                nameState.typeText(cropsInfo.name)
                cropsInfo.wateringInterval
                    ?.let { wateringIntervalState.setDay(it) }
                    ?: run { wateringIntervalState.toggleDisabled(true) }
                growingDayState.setDay(cropsInfo.growingDay)
            } ?: run {
                cropsInfoItem = CropsInfoItemUiModel.CUSTOM
                wateringIntervalState.toggleDisabled(false)
                growingDayState.toggleDisabled(false)
            }
    }

    private suspend fun setCropsData(cropsId: String) {
        getCropsFlowUseCase(cropsId)
            .first()
            .let { crops ->
                cropsInfoItem = crops.toItemUiModel()
                plantingDate = crops.plantingDate
                nameState.typeText(crops.name)
                nickNameState.typeText(crops.nickName)
                crops.wateringInterval
                    ?.let { wateringIntervalState.setDay(it) }
                    ?: run { wateringIntervalState.toggleDisabled(true) }
                crops.growingDay
                    ?.let { growingDayState.setDay(it) }
                    ?: run { growingDayState.toggleDisabled(true) }
            }
    }

    private suspend fun getCropsInfoList(): Result<AddCropsUiState.ShowCropsTypeSheet> = runCatching {
        val monthlyCropsList =
            getRecommendedCropsInfoListUseCase()
                .getOrThrow()
                .map(CropsInfo::toItemUiModel)
        val cropsInfoList =
            listOf(CropsInfoItemUiModel.CUSTOM) +
            getCropsInfoListUseCase()
                .getOrThrow()
                .map(CropsInfo::toItemUiModel)
        AddCropsUiState.ShowCropsTypeSheet(monthlyCropsList, cropsInfoList)
    }

    fun showCropsTypeSheet() {
        viewModelScope.launch {
            getCropsInfoList()
                .onSuccess { uiState = it }
        }
    }

    fun showDatePicker() {
        uiState = AddCropsUiState.ShowDatePicker(plantingDate)
    }

    fun dismiss() {
        uiState = AddCropsUiState.Idle
    }

    fun onCropsTypeSelected(cropsKey: CropsKey) {
        viewModelScope.launch {
            setCropsInfoData(cropsKey)
        }
    }

    fun onDateSelected(date: String) {
        plantingDate = date
        dismiss()
    }

    fun validate(): Boolean {
        if (cropsInfoItem == null) {
            return false
        }
        return if (cropsInfoItem?.key == CropsKey.CUSTOM) {
            nickNameState.isValidate() && wateringIntervalState.isValidate()
        } else {
            nameState.isValidate() &&
                    nickNameState.isValidate() &&
                    wateringIntervalState.isValidate() &&
                    growingDayState.isValidate()
        }
    }


    fun onButton(moveCropsDetail: (String, String) -> Unit) {
        viewModelScope.launch {
            uiState = AddCropsUiState.Loading
            route.cropsKey?.let { addCrops(moveCropsDetail) }
            route.cropsId?.let { id -> editCrops(id, moveCropsDetail) }
        }
    }

    private suspend fun addCrops(moveCropsDetail: (String, String) -> Unit) {
        val addCropsRequest = AddCropsRequest(
            key = cropsInfoItem!!.key,
            name = nameState.getTrimmedText(),
            nickName = nickNameState.getTrimmedText(),
            plantingDate = plantingDate,
            wateringInterval = wateringIntervalState.getTypedDay(),
            growingDay = if (cropsInfoItem?.key == CropsKey.CUSTOM) {
                growingDayState.getTypedDay()
            } else {
                cropsInfoItem?.growingDay
            },
        )
        addCropsUseCase(addCropsRequest)
            .onSuccess { id ->
                moveCropsDetail(id, addCropsRequest.name)
                // ${addCropsRequest.nickName}을/를 추가했어요
            }
            .onFailure {
                uiState = AddCropsUiState.Idle
                // 작물 추가에 실패했어요
            }

    }

    private suspend fun editCrops(cropsId: String, moveCropsDetail: (String, String) -> Unit) {
        val updateCropsRequest = UpdateCropsRequest(
            id = cropsId,
            name = nameState.getTrimmedText(),
            nickName = nickNameState.getTrimmedText(),
            plantingDate = plantingDate,
            wateringInterval = wateringIntervalState.getTypedDay(),
            growingDay = if (cropsInfoItem?.key == CropsKey.CUSTOM) {
                growingDayState.getTypedDay()
            } else {
                cropsInfoItem?.growingDay
            },
        )
        updateCropsUseCase(updateCropsRequest)
            .onSuccess { id ->
                moveCropsDetail(id, updateCropsRequest.name)
                // ${updateCropsRequest.nickName}을/를 수정했어요
            }
            .onFailure {
                uiState = AddCropsUiState.Idle
                // 작물 수정에 실패했어요

            }
    }
}