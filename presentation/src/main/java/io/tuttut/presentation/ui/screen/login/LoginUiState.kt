package io.tuttut.presentation.ui.screen.login

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data object Nothing : LoginUiState
}