package io.tuttut.presentation.ui.screen.main.changeProfile

interface ChangeProfileUiState {
    data object Idle : ChangeProfileUiState

    data object Loading : ChangeProfileUiState
}