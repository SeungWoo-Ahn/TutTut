package io.tuttut.presentation.ui.screen.main.my

import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User

sealed interface MyUiState {
    data object Loading : MyUiState
    data class Success(
        val user: User,
        val garden: Garden
    ) : MyUiState
}