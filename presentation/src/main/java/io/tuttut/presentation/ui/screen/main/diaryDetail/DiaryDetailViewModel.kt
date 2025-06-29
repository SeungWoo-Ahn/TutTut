package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.comment.CommentWithAuthor
import io.tuttut.domain.usecase.comment.AddCommentUseCase
import io.tuttut.domain.usecase.comment.DeleteCommentUseCase
import io.tuttut.domain.usecase.comment.GetCommentListFlowUseCase
import io.tuttut.domain.usecase.diary.DeleteDiaryUseCase
import io.tuttut.domain.usecase.diary.GetDiaryFlowUseCase
import io.tuttut.domain.usecase.user.GetCurrentUserUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toUiModel
import io.tuttut.presentation.model.UserUiModel
import io.tuttut.presentation.navigation.MainScreen
import io.tuttut.presentation.ui.state.TextFieldState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getDiaryFlowUseCase: GetDiaryFlowUseCase,
    getCommentListFlowUseCase: GetCommentListFlowUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val diaryId = savedStateHandle.toRoute<MainScreen.DiaryDetail>().diaryId

    val uiState: StateFlow<DiaryDetailUiState> =
        combine(
            flow = getDiaryFlowUseCase(diaryId),
            flow2 = getCommentListFlowUseCase(diaryId),
        ) { diary, commentList ->
            val currentUser = getCurrentUserUseCase().getOrNull()?.toUiModel()
                    ?: UserUiModel.WITHDREW
            DiaryDetailUiState.Success(
                currentUser = currentUser,
                diary = diary.toUiModel(),
                commentList = commentList.map(CommentWithAuthor::toUiModel)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DiaryDetailUiState.Loading
        )

    val commentState = TextFieldState(20)

    var showDeleteSheet by mutableStateOf(false)
        private set
    var showReportSheet by mutableStateOf(false)
        private set

    fun setDeleteSheetState(state: Boolean) {
        showDeleteSheet = state
    }

    fun setReportSheetState(state: Boolean) {
        showReportSheet = state
    }

    fun onSend() {
        viewModelScope.launch {
            addCommentUseCase(diaryId, commentState.getTrimmedText())
                .onSuccess { commentState.resetText() }
                .onFailure {
                    // 댓글 추가에 실패했어요
                }
        }
    }

    fun onDeleteComment(id: String) {
        viewModelScope.launch {
            deleteCommentUseCase(diaryId, id)
                .onFailure {
                    // 댓글 삭제에 실패했어요
                }
        }
    }

    fun onDelete(moveBack: () -> Unit) {
        viewModelScope.launch {
            deleteDiaryUseCase(diaryId)
                .onSuccess { moveBack() }
                .onFailure {
                    // 일지 삭제에 실패했어요
                }
        }
    }

    fun onReport(reason: String) {
        // "${reason}로 신고했어요"
    }
}