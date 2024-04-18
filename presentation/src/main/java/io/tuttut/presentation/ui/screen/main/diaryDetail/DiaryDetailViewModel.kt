package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val commentRepo: CommentRepository,
    private val diaryRepo: DiaryRepository,
    private val storageRepo: StorageRepository,
    authRepository: AuthRepository,
    gardenRepo: GardenRepository,
    private val diaryModel: DiaryModel,
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

    val comments: Flow<PagingData<Comment>>
        = commentRepo.getDiaryComments(currentUser.gardenId, diary.id).cachedIn(viewModelScope)

    private val _commentUiState = MutableStateFlow<CommentUiState>(CommentUiState.Nothing)
    val commentUiState: StateFlow<CommentUiState> = _commentUiState

    var showDeleteSheet by mutableStateOf(false)
    var showReportSheet by mutableStateOf(false)

    private val _typedComment = MutableStateFlow("")
    val typedComment: StateFlow<String> = _typedComment

    fun typeComment(text: String) {
        if (text.length < 200) {
            _typedComment.value = text
        }
    }

    fun onSend(onShowSnackBar: suspend (String, String?) -> Boolean, hideKeyBoard: () -> Unit, refresh: () -> Unit) {
        viewModelScope.launch {
            hideKeyBoard()
            val comment = Comment(
                authorId = currentUser.id,
                created = getCurrentDateTime(),
                content = typedComment.value.trim()
            )
            commentRepo.addDiaryComment(currentUser.gardenId, diary.id, comment).collect {
                when(it) {
                    Result.Loading -> _commentUiState.value = CommentUiState.Loading
                    is Result.Error -> {
                        onShowSnackBar("댓글 추가에 실패했어요", null)
                    }
                    is Result.Success -> {
                        refresh()
                        _typedComment.value = ""
                    }
                    else -> {}
                }
                _commentUiState.value = CommentUiState.Nothing
            }
        }
    }

    fun onEdit(diary: Diary, moveEditDiary: () -> Unit) {
        diaryModel.observeDiary(diary, true)
        moveEditDiary()
    }

    fun onDelete(diary: Diary, moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            diaryRepo.deleteDiary(currentUser.gardenId, diary).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("일지 삭제에 실패했어요", null)
                    is Result.Success -> {
                        moveBack()
                        onShowSnackBar("일지를 삭제했어요", null)
                    }
                    else -> {}
                }
            }
            withContext(Dispatchers.IO) {
                commentRepo.deleteAllDiaryComments(currentUser.gardenId, diary.id)
                storageRepo.deleteAllImages(diary.imgUrlList)
            }
        }
    }

    fun onReport(reason: String, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            showReportSheet = false
            onShowSnackBar("${reason}로 신고했어요", null)
        }
    }

    fun onDeleteComment(commentId: String, onShowSnackBar: suspend (String, String?) -> Boolean, refresh: () -> Unit) {
        viewModelScope.launch {
            commentRepo.deleteDiaryComment(currentUser.gardenId, diary.id, commentId).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("댓글 삭제에 실패했어요", null)
                    is Result.Success -> {
                        refresh()
                    }
                    else -> {}
                }
            }
        }
    }
}