package io.tuttut.presentation.ui.screen.main.changeProfile

interface ChangeProfileUiState {
    data object Loading : ChangeProfileUiState
    data object Nothing : ChangeProfileUiState
}

fun ChangeProfileUiState.isLoading(): Boolean = this == ChangeProfileUiState.Loading