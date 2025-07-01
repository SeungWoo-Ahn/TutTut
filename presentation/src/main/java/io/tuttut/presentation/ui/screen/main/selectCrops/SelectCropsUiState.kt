package io.tuttut.presentation.ui.screen.main.selectCrops

import io.tuttut.presentation.model.CropsInfoUiModel

sealed interface SelectCropsUiState {
    data object Loading : SelectCropsUiState

    data class Success(
        val monthlyCropsList: List<CropsInfoUiModel>,
        val cropsInfoList: List<CropsInfoUiModel>,
    ) : SelectCropsUiState
}