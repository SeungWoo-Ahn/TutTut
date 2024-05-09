package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.tuttut.data.model.dto.Diary

sealed interface DiaryListUiState {
    data object Loading : DiaryListUiState
    data class Success(
        val diaryList: List<Diary>
    ) : DiaryListUiState
}

class DiaryListDeleteSheetState {
    var showSheet by mutableStateOf(false)
    var focusedDiary = Diary()
    fun show(diary: Diary) {
        focusedDiary = diary
        showSheet = true
    }

    fun dismiss() {
        showSheet = false
    }
}