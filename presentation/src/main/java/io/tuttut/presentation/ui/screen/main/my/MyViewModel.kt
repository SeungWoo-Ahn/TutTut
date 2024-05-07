package io.tuttut.presentation.ui.screen.main.my

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.model.UserModel
import io.tuttut.presentation.util.LinkUtil
import io.tuttut.presentation.util.ShareUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    authRepo: AuthRepository,
    private val gardenRepo: GardenRepository,
    private val userModel: UserModel,
    private val shareUtil: ShareUtil,
    private val linkUtil: LinkUtil,
    private val pref: PreferenceUtil
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value
    val memberList = gardenRepo.gardenMemberInfo

    val uiState: StateFlow<MyUiState>
        = authRepo.getUser(pref.userId)
            .combine(
                flow = gardenRepo.getGardenInfo(pref.gardenId)
            ) { user, garden ->
                MyUiState.Success(user, garden)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MyUiState.Loading
            )

    fun shareGarden(context: Context, garden: Garden) = shareUtil.shareGarden(context, garden, currentUser.name)

    fun openBrowser(context: Context, url: String) = linkUtil.openBrowser(context, url)

    fun refreshMember() {
        useFlag(userModel.refreshMember) {
            viewModelScope.launch {
                gardenRepo.getGardenMemberInfo(pref.gardenId).collect()
            }
        }
    }
}