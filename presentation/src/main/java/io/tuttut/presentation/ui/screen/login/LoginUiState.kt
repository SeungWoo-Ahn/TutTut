package io.tuttut.presentation.ui.screen.login

import io.tuttut.domain.model.user.JoinRequest

sealed interface LoginUiState {
    data object Idle : LoginUiState

    data object Loading : LoginUiState

    sealed class PolicySheetState(
        open val policyChecked: Boolean,
        open val personalChecked: Boolean,
    ) : LoginUiState {
        data class Idle(
            val joinRequest: JoinRequest,
            override val policyChecked: Boolean = false,
            override val personalChecked: Boolean = false,
        ) : PolicySheetState(policyChecked, personalChecked)

        data object Loading : PolicySheetState(true, true)
    }
}