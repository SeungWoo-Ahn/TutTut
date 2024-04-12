package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.comment.CommentRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryRepo: DiaryRepository,
    private val commentRepo: CommentRepository,
    private val storageRepo: StorageRepository,
    authRepo: AuthRepository,
    gardenRepo: GardenRepository,
    private val cropsModel: CropsModel,
    private val diaryModel: DiaryModel,
    prefs: PreferenceUtil,
) : BaseViewModel() {
    val crops = cropsModel.observedCrops.value
    val currentUser = authRepo.currentUser.value
    val memberMap = gardenRepo.gardenMemberMap

    val diaryList: Flow<PagingData<Diary>> = diaryRepo.getDiaryList(prefs.gardenId, crops.id).cachedIn(viewModelScope)

    private var selectedDiary by mutableStateOf(Diary())
    var showDeleteDialog by mutableStateOf(false)

    fun onDiary(diary: Diary, moveDiary: () -> Unit) {
        diaryModel.observeDiary(diary)
        moveDiary()
    }

    fun onEdit(diary: Diary, moveEditDiary: () -> Unit) {
        diaryModel.observeDiary(diary, true)
        moveEditDiary()
    }

    fun showDeleteDialog(diary: Diary) {
        selectedDiary = diary
        showDeleteDialog = true
    }

    fun onDelete(diaryList: LazyPagingItems<Diary>, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            diaryRepo.deleteDiary(currentUser.gardenId, selectedDiary).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("일지 삭제에 실패했어요", null)
                    is Result.Success -> {
                        diaryList.refresh()
                        cropsModel.refreshCropsList()
                    }
                    else -> {}
                }
            }
            withContext(Dispatchers.IO) {
                commentRepo.deleteAllDiaryComments(currentUser.gardenId, selectedDiary.id)
                storageRepo.deleteAllImages(selectedDiary.imgUrlList)
            }
        }
    }

    fun onReport() {

    }

    fun refreshDiaryList(diaryList: LazyPagingItems<Diary>) {
        useFlag(diaryModel.refreshDiaryList) {
            diaryList.refresh()
        }
    }
}