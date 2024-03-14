package io.tuttut.presentation.ui.screen.main

sealed interface MainUiState {
    data object Loading: MainUiState
    data object Nothing: MainUiState
}