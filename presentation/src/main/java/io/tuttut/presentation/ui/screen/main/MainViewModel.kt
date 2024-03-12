package io.tuttut.presentation.ui.screen.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Response
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.component.MainTab
import io.tuttut.presentation.util.getCurrentMonth
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository
): BaseViewModel() {
    init {
        cachingCropsInfo()
    }

    private val _uiState = mutableStateOf<MainUiState>(MainUiState.Loading)
    val uiState: State<MainUiState> = _uiState

    private val _selectedTab = mutableStateOf(MainTab.GROWING)
    val selectedTab: State<MainTab> = _selectedTab

    val gardenName = "텃밭 이름"

    private fun cachingCropsInfo() = viewModelScope.launch {
        val result = cropsInfoRepo.cachingCropsInfo(getCurrentMonth())
        if (result is Response.Success) {
            if (result.data) {
                _uiState.value = MainUiState.Nothing
            }
        }
    }

    fun onTab(tab: MainTab) {
        _selectedTab.value = tab
    }
}