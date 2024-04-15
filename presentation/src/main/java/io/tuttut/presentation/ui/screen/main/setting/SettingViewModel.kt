package io.tuttut.presentation.ui.screen.main.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    authRepo: AuthRepository,
    private val gardenRepo: GardenRepository,
    private val pref: PreferenceUtil
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value

    private val _uiState = MutableStateFlow<SettingUiState>(SettingUiState.Nothing)
    val uiState: StateFlow<SettingUiState> = _uiState

    var showQuitSheet by mutableStateOf(false)
    var showWithdrawSheet by mutableStateOf(false)

    fun quitGarden(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (uiState.value.isLoading()) return
        viewModelScope.launch {
            gardenRepo.quitGarden(currentUser.id, currentUser.gardenId).collect {
                when (it) {
                    Result.Loading -> _uiState.value = SettingUiState.Loading
                    is Result.Error -> onShowSnackBar("요청에 실패했어요", null)
                    is Result.Success -> {
                        pref.clear()
                        moveLogin()
                        onShowSnackBar("텃밭에서 나갔어요", null)
                    }
                    else -> {}
                }
                _uiState.value = SettingUiState.Nothing
            }
        }
    }

    fun signOut(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (uiState.value.isLoading()) return
        viewModelScope.launch {
            authClient.signOut()
            pref.clear()
            moveLogin()
            onShowSnackBar("정상적으로 로그아웃 됐어요", null)
        }
    }

    fun withDraw(moveLogin: () -> Unit) {
        if (uiState.value.isLoading()) return
    }
}