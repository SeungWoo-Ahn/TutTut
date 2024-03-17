package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CropsDetailViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    cropsRepo: CropsRepository,
    cropsModel: CropsModel,
    prefs: PreferenceUtil
): BaseViewModel() {
    val uiState: StateFlow<CropsDetailUiState>
        = cropsRepo.getCropsDetail(
        gardenId = prefs.gardenId,
        cropsId = cropsModel.selectedCropsId.value
    ).map(CropsDetailUiState::Success)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CropsDetailUiState.Loading
    )

    fun onHarvest() {

    }

    fun onMoveCropsInfo() {

    }

    fun onDiary() {

    }

    fun onWatering() {

    }

    fun moveAddDiary() {

    }
}