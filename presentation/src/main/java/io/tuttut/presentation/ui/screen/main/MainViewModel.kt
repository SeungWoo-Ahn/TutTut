package io.tuttut.presentation.ui.screen.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.component.MainTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val gardenRepo: GardenRepository,
    val cropsInfoRepo: CropsInfoRepository,
    cropsRepo: CropsRepository,
    private val prefs: PreferenceUtil,
    private val cropsModel: CropsModel,
): BaseViewModel() {
    private val _selectedTab = MutableStateFlow(MainTab.GROWING)
    val selectedTab: StateFlow<MainTab> = _selectedTab

    val cropsList: Flow<PagingData<Crops>> =
        cropsRepo.getGardenCropsList(prefs.gardenId, false).cachedIn(viewModelScope)

    val harvestedCropsList: Flow<PagingData<Crops>> =
        cropsRepo.getGardenCropsList(prefs.gardenId, true).cachedIn(viewModelScope)

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

    fun refreshCropsList(cropsList: LazyPagingItems<Crops>) {
        useFlag(cropsModel.refreshCropsList) {
            cropsList.refresh()
        }
    }

    fun refreshHarvestedCropsList(cropsList: LazyPagingItems<Crops>) {
        useFlag(cropsModel.refreshHarvestedCropsList) {
            cropsList.refresh()
        }
    }

    suspend fun cachingGardenInfo() {
        gardenRepo.getGardenMemberInfo(prefs.gardenId).collect()
        authRepo.getUserInfo(authClient.getSignedInUser()!!.userId).collect()
    }
}
