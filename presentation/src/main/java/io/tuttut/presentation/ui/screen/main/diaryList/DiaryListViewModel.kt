package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    diaryRepo: DiaryRepository,
    cropsModel: CropsModel,
    prefs: PreferenceUtil,
) : BaseViewModel() {
    val crops = cropsModel.observedCrops.value
    val diaryList: Flow<PagingData<Diary>> =
        diaryRepo.getDiaryList(prefs.gardenId, crops.id).cachedIn(viewModelScope)
}