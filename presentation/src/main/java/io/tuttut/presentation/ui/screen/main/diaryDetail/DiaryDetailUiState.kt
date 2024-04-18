package io.tuttut.presentation.ui.screen.main.diaryDetail

import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary

sealed interface DiaryDetailUiState {
    data object Loading : DiaryDetailUiState
    data class Success(
        val diary: Diary,
        val comments: List<Comment>
    ) : DiaryDetailUiState
}

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data object Nothing : CommentUiState
}