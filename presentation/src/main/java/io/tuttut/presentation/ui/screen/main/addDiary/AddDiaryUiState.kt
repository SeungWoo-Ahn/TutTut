package io.tuttut.presentation.ui.screen.main.addDiary

sealed interface AddDiaryUiState {
    data object Idle : AddDiaryUiState

    data object Loading : AddDiaryUiState
}