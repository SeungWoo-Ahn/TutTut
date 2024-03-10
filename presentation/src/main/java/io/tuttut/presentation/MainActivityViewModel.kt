package io.tuttut.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.navigation.ScreenGraph
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): BaseViewModel() {
    fun getStartDestination(): ScreenGraph = if (authClient.getSignedInUser() == null) ScreenGraph.LoginGraph else ScreenGraph.MainGraph
}