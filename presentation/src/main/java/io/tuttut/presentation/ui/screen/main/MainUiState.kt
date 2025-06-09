package io.tuttut.presentation.ui.screen.main

import io.tuttut.data.network.model.Crops
import io.tuttut.data.network.model.Garden

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val cropList: List<Crops>
    ) : MainUiState
}

sealed interface MainTopBarState {
    data object Loading : MainTopBarState
    data class Success(
        val garden: Garden
    ) : MainTopBarState
}