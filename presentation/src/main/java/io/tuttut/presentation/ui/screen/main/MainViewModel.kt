package io.tuttut.presentation.ui.screen.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.component.MainTab
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
): BaseViewModel() {

    private val _uiState = mutableStateOf<MainUiState>(MainUiState.Loading)
    val uiState: State<MainUiState> = _uiState

    private val _selectedTab = mutableStateOf(MainTab.GROWING)
    val selectedTab: State<MainTab> = _selectedTab

    val gardenName = "텃밭 이름"

    fun onTab(tab: MainTab) {
        _selectedTab.value = tab
    }
}