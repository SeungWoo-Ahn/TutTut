package io.tuttut.presentation.ui.screen.main.addCrops

import io.tuttut.presentation.model.CropsInfoItemUiModel

interface AddCropsUiState {
    data object Idle : AddCropsUiState

    data class ShowCropsTypeSheet(
        val monthlyCropsList: List<CropsInfoItemUiModel>,
        val cropsInfoList: List<CropsInfoItemUiModel>,
    ) : AddCropsUiState

    data class ShowDatePicker(
        val plantingDate: String,
    ) : AddCropsUiState

    data object Loading : AddCropsUiState
}