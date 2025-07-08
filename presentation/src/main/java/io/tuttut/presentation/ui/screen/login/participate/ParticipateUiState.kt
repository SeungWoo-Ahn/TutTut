package io.tuttut.presentation.ui.screen.login.participate

import io.tuttut.domain.model.garden.Garden

sealed interface ParticipateUiState {
    data object Idle : ParticipateUiState

    data object Loading : ParticipateUiState

    sealed class DialogState(
        open val garden: Garden
    ) : ParticipateUiState {
        data class Idle(override val garden: Garden) : DialogState(garden)

        data class Loading(override val garden: Garden) : DialogState(garden)
    }
}

enum class ParticipateTab {
    CREATE, JOIN
}