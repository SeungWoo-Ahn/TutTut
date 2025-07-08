package io.tuttut.presentation.ui.screen.main.selectCrops

import io.tuttut.presentation.model.CropsInfoItemUiModel

sealed interface SelectCropsUiState {
    data object Loading : SelectCropsUiState

    data class Success(
        val monthlyCropsList: List<CropsInfoItemUiModel>,
        val cropsInfoList: List<CropsInfoItemUiModel>,
    ) : SelectCropsUiState
}