package io.tuttut.presentation.ui.screen.main

import io.tuttut.data.model.dto.Garden

sealed interface MainTopBarState {
    data object Loading : MainTopBarState
    data class Success(
        val garden: Garden
    ) : MainTopBarState
}