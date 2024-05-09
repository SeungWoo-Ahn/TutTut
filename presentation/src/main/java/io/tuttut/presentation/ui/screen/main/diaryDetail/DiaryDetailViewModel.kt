package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.BottomSheetState
import io.tuttut.presentation.ui.state.EditTextState
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    authRepo: AuthRepository,
    private val commentRepo: CommentRepository,
    private val diaryRepo: DiaryRepository,
    private val storageRepo: StorageRepository,
    gardenRepo: GardenRepository,
    private val diaryModel: DiaryModel,
    private val pref: PreferenceUtil
) : BaseViewModel() {
    val memberMap = gardenRepo.gardenMemberMap
    val diary = diaryModel.observedDiary.value

    val uiState: StateFlow<DiaryDetailUiState>
        = combine(
            flow = authRepo.getUser(pref.userId),
            flow2 = diaryRepo.getDiaryDetail(pref.gardenId, diary.id),
            flow3 = commentRepo.getDiaryComments(pref.gardenId, diary.id)
        ) { user, diary, comments -> DiaryDetailUiState.Success(user, diary, comments) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DiaryDetailUiState.Loading
        )

    val commentState = EditTextState(maxLength = 200)
    val deleteSheetState = BottomSheetState()
    val reportSheetState = BottomSheetState()

    fun onSend(hideKeyBoard: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (commentState.typedText.trim().isEmpty()) return
        viewModelScope.launch {
            hideKeyBoard()
            val comment = Comment(
                authorId = pref.userId,
                created = getCurrentDateTime(),
                content = commentState.typedText.trim()
            )
            commentRepo.addDiaryComment(pref.gardenId, diary.id, comment).collect {
                when(it) {
                    is Result.Error -> onShowSnackBar("댓글 추가에 실패했어요", null)
                    is Result.Success ->  commentState.resetText()
                    else -> {}
                }
            }
        }
    }

    fun onEdit(moveEditDiary: () -> Unit) {
        diaryModel.observeDiary(diary, true)
        moveEditDiary()
    }

    fun onDelete(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            diaryRepo.deleteDiary(pref.gardenId, diary).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("일지 삭제에 실패했어요", null)
                    is Result.Success -> {
                        withContext(Dispatchers.IO) {
                            commentRepo.deleteAllDiaryComments(pref.gardenId, diary.id)
                            storageRepo.deleteAllImages(diary.imgUrlList)
                        }
                        moveBack()
                        onShowSnackBar("일지를 삭제했어요", null)
                    }
                    else -> {}
                }
            }
        }
    }

    fun onReport(reason: String, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            reportSheetState.dismiss()
            onShowSnackBar("${reason}로 신고했어요", null)
        }
    }

    fun onDeleteComment(commentId: String, clearFocus: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            clearFocus()
            commentRepo.deleteDiaryComment(pref.gardenId, diary.id, commentId).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("댓글 삭제에 실패했어요", null)
                    else -> {}
                }
            }
        }
    }
}