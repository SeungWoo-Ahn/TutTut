package io.tuttut.presentation.ui.screen.main.diaryDetail

import io.tuttut.presentation.model.CommentUiModel
import io.tuttut.presentation.model.DiaryUiModel
import io.tuttut.presentation.model.UserUiModel

sealed interface DiaryDetailUiState {
    data object Loading : DiaryDetailUiState

    data class Success(
        val currentUser: UserUiModel,
        val diary: DiaryUiModel,
        val commentList: List<CommentUiModel>
    ) : DiaryDetailUiState
}

sealed interface DiaryDetailSheetState {
    data object Idle : DiaryDetailSheetState

    data object ShowReportSheet : DiaryDetailSheetState

    data object ShowDeleteSheet : DiaryDetailSheetState
}