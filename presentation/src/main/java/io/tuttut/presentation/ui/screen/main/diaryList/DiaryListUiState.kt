package io.tuttut.presentation.ui.screen.main.diaryList

import io.tuttut.data.network.model.Diary

sealed interface DiaryListUiState {
    data object Loading : DiaryListUiState
    data class Success(
        val diaryList: List<Diary>
    ) : DiaryListUiState
}