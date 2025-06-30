package io.tuttut.presentation.ui.screen.main.my

import io.tuttut.presentation.model.GardenUiModel
import io.tuttut.presentation.model.UserUiModel

sealed interface MyUiState {
    data object Loading : MyUiState

    data class Success(
        val user: UserUiModel,
        val garden: GardenUiModel,
    ) : MyUiState
}