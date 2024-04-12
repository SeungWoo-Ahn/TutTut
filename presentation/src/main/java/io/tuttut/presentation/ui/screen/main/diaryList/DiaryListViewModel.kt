package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    diaryRepo: DiaryRepository,
    authRepo: AuthRepository,
    gardenRepo: GardenRepository,
    cropsModel: CropsModel,
    private val diaryModel: DiaryModel,
    prefs: PreferenceUtil,
) : BaseViewModel() {
    val crops = cropsModel.observedCrops.value
    val currentUser = authRepo.currentUser.value
    val memberMap = gardenRepo.gardenMemberMap

    val diaryList: Flow<PagingData<Diary>> =
        diaryRepo.getDiaryList(prefs.gardenId, crops.id).cachedIn(viewModelScope)
    var showDeleteDialog = mutableStateOf(false)
        private set

    fun onDiary(diary: Diary, moveDiary: () -> Unit) {
        diaryModel.observeDiary(diary)
        moveDiary()
    }

    fun onEdit() {

    }

    fun onDelete() {

    }

    fun onReport() {

    }

    fun refreshDiaryList(diaryList: LazyPagingItems<Diary>) {
        useFlag(diaryModel.refreshDiaryList) {
            diaryList.refresh()
        }
    }
}