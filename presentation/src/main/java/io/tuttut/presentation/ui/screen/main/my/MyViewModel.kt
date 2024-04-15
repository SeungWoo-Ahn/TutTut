package io.tuttut.presentation.ui.screen.main.my

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.util.ShareUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    authRepo: AuthRepository,
    gardenRepo: GardenRepository,
    private val shareUtil: ShareUtil,
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value
    val memberList = gardenRepo.gardenMemberInfo

    val uiState: StateFlow<MyUiState>
        = authRepo.getUser()
            .combine(
                flow = gardenRepo.getGardenInfo(currentUser.gardenId)
            ) { user, garden ->
                MyUiState.Success(user, garden)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MyUiState.Loading
            )

    fun shareGarden(context: Context, garden: Garden) = shareUtil.shareGarden(context, garden, currentUser.name)
}