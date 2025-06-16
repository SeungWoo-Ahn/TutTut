package io.tuttut.presentation.ui.screen.main

import io.tuttut.presentation.model.crops.MainCropsUiModel

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(
        val cropList: List<MainCropsUiModel>
    ) : MainUiState
}

sealed interface MainTopBarState {
    data object Loading : MainTopBarState
    data class Success(
        val gardenName: String
    ) : MainTopBarState
}

enum class MainTab(val index: Int, val title: String) {
    GROWING(0, "재배중"),
    HARVESTED(1, "수확 완료")
}