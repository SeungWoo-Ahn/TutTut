package io.tuttut.presentation.ui.screen.main.diaryDetail

import io.tuttut.data.network.model.CommentDto
import io.tuttut.data.network.model.DiaryDto
import io.tuttut.data.network.model.UserDto

sealed interface DiaryDetailUiState {
    data object Loading : DiaryDetailUiState
    data class Success(
        val currentUser: UserDto,
        val diary: DiaryDto,
        val comments: List<CommentDto>
    ) : DiaryDetailUiState
}

sealed interface CommentUiState {
    data object Loading : CommentUiState
    data object Nothing : CommentUiState
}