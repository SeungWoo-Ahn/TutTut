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
import io.tuttut.presentation.mapper.toItemUiModel
import kotlinx.coroutines.launch
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

    private suspend fun getInitData(): Result<SelectCropsUiState.Success> = runCatching {
        val monthlyCropsList =
            getRecommendedCropsInfoListUseCase()
                .getOrThrow()
                .map(CropsInfo::toItemUiModel)
        val cropsInfoList =
            getCropsInfoListUseCase()
                .getOrThrow()
                .map(CropsInfo::toItemUiModel)
        SelectCropsUiState.Success(monthlyCropsList, cropsInfoList)
    }
}