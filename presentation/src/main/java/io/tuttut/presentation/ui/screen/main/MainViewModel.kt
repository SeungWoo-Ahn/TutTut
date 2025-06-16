package io.tuttut.presentation.ui.screen.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.crops.Crops
import io.tuttut.domain.usecase.crops.GetCropListFlowUseCase
import io.tuttut.domain.usecase.garden.GetGardenUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toMainCropsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCropListFlowUseCase: GetCropListFlowUseCase,
    private val getGardenUseCase: GetGardenUseCase,
): BaseViewModel() {
    private val _selectedTab = MutableStateFlow(MainTab.GROWING)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    val uiState: StateFlow<MainUiState> =
        selectedTab
            .flatMapLatest { tab ->
                getCropListFlowUseCase(
                    isHarvest = tab == MainTab.HARVESTED
                )
            }
            .map { cropsList ->
                cropsList.map(Crops::toMainCropsUiModel)
            }
            .map(MainUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState.Loading
            )

    var topBarState by mutableStateOf<MainTopBarState>(MainTopBarState.Loading)
        private set

    init {
        viewModelScope.launch {
            getGardenUseCase()
                .onSuccess { garden ->
                    topBarState = MainTopBarState.Success(garden.name)
                }
        }
    }

    fun onTab(tab: MainTab) {
        _selectedTab.update { tab }
    }
}
