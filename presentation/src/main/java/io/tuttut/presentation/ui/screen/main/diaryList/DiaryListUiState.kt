package io.tuttut.presentation.ui.screen.main.diaryList

import io.tuttut.presentation.model.DiaryListItemUiModel

sealed interface DiaryListUiState {
    data object Loading : DiaryListUiState

    data class Success(
        val diaryList: List<DiaryListItemUiModel>
    ) : DiaryListUiState
}

sealed interface DiaryListSheetState {
    data object Idle : DiaryListSheetState

    data object ShowReportSheet : DiaryListSheetState

    data class ShowDeleteSheet(val id: String) : DiaryListSheetState
}