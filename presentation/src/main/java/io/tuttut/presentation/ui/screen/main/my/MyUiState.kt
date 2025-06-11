package io.tuttut.presentation.ui.screen.main.my

import io.tuttut.data.network.model.GardenDto
import io.tuttut.data.network.model.UserDto

sealed interface MyUiState {
    data object Loading : MyUiState
    data class Success(
        val user: UserDto,
        val garden: GardenDto
    ) : MyUiState
}