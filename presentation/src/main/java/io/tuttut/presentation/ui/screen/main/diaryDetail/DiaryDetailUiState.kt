package io.tuttut.presentation.ui.screen.main.diaryDetail

import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.User

sealed interface DiaryDetailUiState {
    data object Loading : DiaryDetailUiState
    data class Success(
        val currentUser: User,
        val diary: Diary,
        val comments: List<Comment>
    ) : DiaryDetailUiState
}

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data object Nothing : CommentUiState
}