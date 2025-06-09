package io.tuttut.presentation.ui.screen.login.participate

import io.tuttut.data.network.model.GardenDto


sealed interface ParticipateUiState {
    data object Loading : ParticipateUiState
    data object Nothing : ParticipateUiState
}

data class ParticipateDialogUiState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val content: GardenDto = GardenDto()
)