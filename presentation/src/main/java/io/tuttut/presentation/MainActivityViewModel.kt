package io.tuttut.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.util.getCurrentMonth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val cropsInfoRepo: CropsInfoRepository,
): BaseViewModel() {
    fun getStartDestination() = if (authClient.getSignedInUser() != null) ScreenGraph.MainGraph else ScreenGraph.LoginGraph

    fun getInitialInfo() {
        viewModelScope.launch {
            cropsInfoRepo.getCropsInfoList(getCurrentMonth()).collect()
        }
    }
}