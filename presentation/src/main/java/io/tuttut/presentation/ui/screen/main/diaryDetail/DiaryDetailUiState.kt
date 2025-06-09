package io.tuttut.presentation.ui.screen.main.diaryDetail

import io.tuttut.data.network.model.CommentDto
import io.tuttut.data.network.model.Diary
import io.tuttut.data.network.model.User

sealed interface DiaryDetailUiState {
    data object Loading : DiaryDetailUiState
    data class Success(
        val currentUser: User,
        val diary: Diary,
        val comments: List<CommentDto>
    ) : DiaryDetailUiState
}

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data object Nothing : CommentUiState
}