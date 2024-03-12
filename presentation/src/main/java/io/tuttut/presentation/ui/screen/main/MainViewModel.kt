package io.tuttut.presentation.ui.screen.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.component.MainTab
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel() {
    private val _selectedTab = mutableStateOf(MainTab.GROWING)
    val selectedTab: State<MainTab> = _selectedTab

    val gardenName = "텃밭 이름"

    fun onTab(tab: MainTab) {
        _selectedTab.value = tab
    }
}