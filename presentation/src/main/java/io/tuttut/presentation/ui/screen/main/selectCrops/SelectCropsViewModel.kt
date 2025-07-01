package io.tuttut.presentation.ui.screen.main.selectCrops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.usecase.cropsInfo.GetCropsInfoListUseCase
import io.tuttut.domain.usecase.cropsInfo.GetRecommendedCropsInfoListUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toUiModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SelectCropsViewModel @Inject constructor(
    private val getCropsInfoListUseCase: GetCropsInfoListUseCase,
    private val getRecommendedCropsInfoListUseCase: GetRecommendedCropsInfoListUseCase,
): BaseViewModel() {
    var uiState by mutableStateOf<SelectCropsUiState>(SelectCropsUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            getInitData()
                .onSuccess { uiState = it }
        }
    }

    private fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }

    private suspend fun getInitData(): Result<SelectCropsUiState.Success> = runCatching {
        val monthlyCropsList =
            getRecommendedCropsInfoListUseCase(getCurrentMonth())
                .getOrThrow()
                .map(CropsInfo::toUiModel)
        val cropsInfoList =
            getCropsInfoListUseCase()
                .getOrThrow()
                .map(CropsInfo::toUiModel)
        SelectCropsUiState.Success(monthlyCropsList, cropsInfoList)
    }
}