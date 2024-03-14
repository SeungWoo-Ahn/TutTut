package io.tuttut.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.util.getCurrentMonth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val cropsInfoRepo: CropsInfoRepository
): BaseViewModel() {

    fun getStartDestination() = if (authClient.getSignedInUser() != null) ScreenGraph.LoginGraph else ScreenGraph.LoginGraph

    fun getInitialInfo() {
        val user = authClient.getSignedInUser()
        viewModelScope.launch {
            cropsInfoRepo.getCropsInfoList(getCurrentMonth()).collect()
            if (user != null) authRepo.getUserInfo(user.userId).collect()
        }
    }
}