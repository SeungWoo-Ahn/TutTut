package io.tuttut.presentation.ui.screen.main.setting

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    authRepo: AuthRepository,
    gardenRepo: GardenRepository,
    private val pref: PreferenceUtil
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value

    fun quitGarden(moveLogin: () -> Unit) {

    }

    fun signOut(moveLogin: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            authClient.signOut()
            pref.clear()
            moveLogin()
            onShowSnackBar("정상적으로 로그아웃 됐어요", null)
        }
    }

    fun withDraw(moveLogin: () -> Unit) {

    }
}