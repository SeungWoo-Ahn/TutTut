package io.tuttut.presentation.ui.screen.main.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.isGoogleProfile
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.ui.screen.main.setting.SettingUiState.*
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.BottomSheetState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val gardenRepo: GardenRepository,
    private val storageRepo: StorageRepository,
    private val pref: PreferenceUtil
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value
    private var _uiState by mutableStateOf<SettingUiState>(Nothing)
    val uiState = _uiState
    val quitSheetState = BottomSheetState()
    val withDrawSheetState = BottomSheetState()

    fun quitGarden(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (uiState.isLoading()) return
        viewModelScope.launch {
            gardenRepo.quitGarden(currentUser.id, currentUser.gardenId).collect {
                when (it) {
                    Result.Loading -> _uiState = Loading
                    is Result.Error -> onShowSnackBar("요청에 실패했어요", null)
                    is Result.Success -> {
                        pref.clear()
                        moveLogin()
                        onShowSnackBar("텃밭에서 나갔어요", null)
                    }
                    else -> {}
                }
                _uiState = Nothing
            }
        }
    }

    fun signOut(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (uiState.isLoading()) return
        viewModelScope.launch {
            authClient.signOut()
            pref.clear()
            moveLogin()
            onShowSnackBar("정상적으로 로그아웃 됐어요", null)
        }
    }

    fun withDraw(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (uiState.isLoading()) return
        viewModelScope.launch {
            deleteProfileImage().run {
                authRepo.withdraw().collect {
                    when (it) {
                        Result.Loading -> _uiState = Loading
                        is Result.Error -> onShowSnackBar("탈퇴 처리에 실패했어요", null)
                        is Result.Success -> {
                            authClient.withdraw()
                            pref.clear()
                            moveLogin()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private suspend fun deleteProfileImage(): Boolean {
        val profile = currentUser.profile
        if (profile.isGoogleProfile()) return true
        return storageRepo.deleteProfileImage(profile.name).first()
    }
}