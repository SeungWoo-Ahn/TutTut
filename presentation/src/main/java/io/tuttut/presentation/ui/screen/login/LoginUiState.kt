package io.tuttut.presentation.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.tuttut.presentation.ui.state.BottomSheetState
import io.tuttut.presentation.ui.state.CheckBoxState

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data object Nothing : LoginUiState
}

sealed interface PolicyUiState {
    data object Loading : PolicyUiState
    data object Nothing : PolicyUiState
}

class PolicySheetState : BottomSheetState() {
    var uiState by mutableStateOf<PolicyUiState>(PolicyUiState.Nothing)
    val policyState = CheckBoxState()
    val personalState = CheckBoxState()

    fun onContinue() {
        uiState = PolicyUiState.Loading
        policyState.onCheckedChange(true)
        personalState.onCheckedChange(true)
    }

    override fun dismiss() {
        super.dismiss()
        policyState.onCheckedChange(false)
        personalState.onCheckedChange(false)
    }

    fun toNothing() {
        uiState = PolicyUiState.Nothing
    }

    fun isLoading() = uiState == PolicyUiState.Loading
}