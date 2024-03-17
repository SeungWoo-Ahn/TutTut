package io.tuttut.presentation.ui.screen.main.cropsDetail

import io.tuttut.data.model.dto.Crops

sealed interface CropsDetailUiState {
    data object Loading : CropsDetailUiState
    data class Success(
        val crops: Crops
    ) : CropsDetailUiState
}