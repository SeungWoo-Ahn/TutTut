package io.tuttut.presentation.ui.screen.main

import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.Garden

sealed interface MainUiState {
    data object Loading: MainUiState
    data class Success(
        val cropsList: List<Crops>
    ) : MainUiState
}

sealed interface MainTopBarState {
    data object Loading : MainTopBarState
    data class Success(
        val garden: Garden
    ) : MainTopBarState
}