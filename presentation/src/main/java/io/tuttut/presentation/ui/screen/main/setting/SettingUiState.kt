package io.tuttut.presentation.ui.screen.main.setting

sealed interface SettingUiState {
    data object Loading : SettingUiState
    data object Nothing : SettingUiState
}

fun SettingUiState.isLoading(): Boolean = this == SettingUiState.Loading