package io.tuttut.presentation.ui.screen.main.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.garden.LeaveGardenUseCase
import io.tuttut.domain.usecase.user.ClearUserDataUseCase
import io.tuttut.domain.usecase.user.WithdrawUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.GoogleAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val leaveGardenUseCase: LeaveGardenUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val googleAuth: GoogleAuth,
) : BaseViewModel() {
    var showLeaveSheet by mutableStateOf(false)
        private set
    var showWithdrawSheet by mutableStateOf(false)
        private set

    fun setLeaveSheetState(state: Boolean) {
        showLeaveSheet = state
    }

    fun setWithdrawSheetState(state: Boolean) {
        showWithdrawSheet = state
    }

    fun leaveGarden(moveLogin: () -> Unit) {
        viewModelScope.launch {
            leaveGardenUseCase()
                .onSuccess {
                    moveLogin()
                    // 텃밭에서 나왔어요
                }
                .onFailure {
                    // 요청에 실패했어요
                }
        }
    }

    fun signOut(moveLogin: () -> Unit) {
        viewModelScope.launch {
            clearUserDataUseCase()
            googleAuth.logout()
            moveLogin()
            // 정상적으로 로그아웃 했어요
        }
    }

    fun withDraw(moveLogin: () -> Unit) {
        viewModelScope.launch {
            withdrawUseCase()
                .onSuccess {
                    googleAuth.withdraw()
                    moveLogin()
                }
                .onFailure {
                    // 탈퇴 처리에 실패했어요
                }
        }
    }
}