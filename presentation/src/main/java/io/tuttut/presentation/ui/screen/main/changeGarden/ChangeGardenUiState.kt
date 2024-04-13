package io.tuttut.presentation.ui.screen.main.changeGarden

sealed interface ChangeGardenUiState {
    data object Loading : ChangeGardenUiState
    data object Nothing : ChangeGardenUiState
}