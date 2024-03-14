package io.tuttut.presentation.ui.screen.login

sealed interface LoginUiState {
    data object Success : LoginUiState
    data object Error : LoginUiState
    data object NeedJoin : LoginUiState
    data object Loading : LoginUiState
    data object Nothing : LoginUiState
}