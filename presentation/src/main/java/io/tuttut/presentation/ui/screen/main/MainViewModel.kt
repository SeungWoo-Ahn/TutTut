package io.tuttut.presentation.ui.screen.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.component.MainTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gardenRepo: GardenRepository,
    val cropsInfoRepo: CropsInfoRepository,
    cropsRepo: CropsRepository,
    private val prefs: PreferenceUtil,
    private val cropsModel: CropsModel,
): BaseViewModel() {
    private val _selectedTab = MutableStateFlow(MainTab.GROWING)
    val selectedTab: StateFlow<MainTab> = _selectedTab

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MainUiState> = selectedTab
        .flatMapLatest { tab ->
            cropsRepo.getGardenCropsList(
                gardenId = prefs.gardenId,
                isHarvested = tab == MainTab.HARVESTED
            )
        }.map(MainUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState.Loading
        )

    val topBarState: StateFlow<MainTopBarState> =
        gardenRepo.getGardenInfo(prefs.gardenId)
            .map(MainTopBarState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainTopBarState.Loading
            )


    fun onTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun onItem(crops: Crops, moveDetail: () -> Unit) {
        cropsModel.observeCrops(crops)
        moveDetail()
    }

    suspend fun cachingGardenInfo() {
        gardenRepo.getGardenMemberInfo(prefs.gardenId).collect()
    }

    fun saveUserId() {
        if (prefs.userId.isEmpty())
            prefs.userId = authClient.getSignedInUser()!!.userId
    }
}
