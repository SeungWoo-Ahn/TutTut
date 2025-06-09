package io.tuttut.presentation.ui.screen.main

import io.tuttut.data.network.model.CropsDto
import io.tuttut.data.network.model.GardenDto

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val cropList: List<CropsDto>
    ) : MainUiState
}

sealed interface MainTopBarState {
    data object Loading : MainTopBarState
    data class Success(
        val garden: GardenDto
    ) : MainTopBarState
}