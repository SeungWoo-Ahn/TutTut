package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.BottomSheetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryRepo: DiaryRepository,
    private val commentRepo: CommentRepository,
    private val storageRepo: StorageRepository,
    gardenRepo: GardenRepository,
    private val diaryModel: DiaryModel,
    val pref: PreferenceUtil,
    cropsModel: CropsModel,
) : BaseViewModel() {
    val crops = cropsModel.observedCrops.value
    val memberMap = gardenRepo.gardenMemberMap

    val uiState: StateFlow<DiaryListUiState>
        = diaryRepo.getDiaryList(pref.gardenId, crops.id)
            .map(DiaryListUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DiaryListUiState.Loading
            )

    val deleteSheetState = DiaryListDeleteSheetState()
    val reportSheetState = BottomSheetState()

    fun onDiary(diary: Diary, moveDiary: () -> Unit) {
        diaryModel.observeDiary(diary)
        moveDiary()
    }

    fun onEdit(diary: Diary, moveEditDiary: () -> Unit) {
        diaryModel.observeDiary(diary, true)
        moveEditDiary()
    }

    fun onDelete(onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            diaryRepo.deleteDiary(pref.gardenId, deleteSheetState.focusedDiary).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("일지 삭제에 실패했어요", null)
                    is Result.Success -> {
                        withContext(Dispatchers.IO) {
                            commentRepo.deleteAllDiaryComments(pref.gardenId, deleteSheetState.focusedDiary.id)
                            storageRepo.deleteAllImages(deleteSheetState.focusedDiary.imgUrlList)
                        }
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
}