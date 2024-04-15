package io.tuttut.presentation.ui.screen.main.addDiary

sealed interface AddDiaryUiState {
    data object Loading : AddDiaryUiState
    data object Nothing : AddDiaryUiState
}

fun AddDiaryUiState.isLoading() = this == AddDiaryUiState.Loading