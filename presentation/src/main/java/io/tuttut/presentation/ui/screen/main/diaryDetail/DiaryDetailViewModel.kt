package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.DiaryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    diaryRepo: DiaryRepository,
    authRepository: AuthRepository,
    gardenRepo: GardenRepository,
    diaryModel: DiaryModel,
) : BaseViewModel() {
    val currentUser = authRepository.currentUser.value
    val memberMap = gardenRepo.gardenMemberMap
    val diary = diaryModel.observedDiary.value

    val uiState: StateFlow<DiaryDetailUiState>
        = diaryRepo.getDiaryDetail(currentUser.gardenId, diary.id)
        .map(DiaryDetailUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DiaryDetailUiState.Loading
        )

    private val _typedComment = MutableStateFlow("")
    val typedComment: StateFlow<String> = _typedComment

    fun typeComment(text: String) {
        _typedComment.value = text
    }

    fun onSend() {

    }

    fun onEdit() {

    }

    fun onDelete() {

    }

    fun onReport() {

    }

    fun onEditComment() {

    }

    fun onDeleteComment() {

    }
}