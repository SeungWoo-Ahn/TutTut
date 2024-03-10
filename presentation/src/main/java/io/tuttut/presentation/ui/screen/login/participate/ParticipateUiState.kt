package io.tuttut.presentation.ui.screen.login.participate


sealed interface ParticipateUiState {
    data object Loading : ParticipateUiState
    data object DialogLoading: ParticipateUiState
    data object Nothing : ParticipateUiState
}