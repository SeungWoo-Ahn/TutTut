package io.tuttut.presentation.ui.screen.main.my

import io.tuttut.data.network.model.Garden
import io.tuttut.data.network.model.User

sealed interface MyUiState {
    data object Loading : MyUiState
    data class Success(
        val user: User,
        val garden: Garden
    ) : MyUiState
}