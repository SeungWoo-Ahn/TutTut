package io.tuttut.presentation.ui.screen.main.changeGarden

sealed interface ChangeGardenUiState {
    data object Idle : ChangeGardenUiState

    data object Loading : ChangeGardenUiState
}