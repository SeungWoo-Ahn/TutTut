package io.tuttut.presentation.ui.screen.login

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.usecase.user.GetUserAndSaveGardenIdUseCase
import io.tuttut.domain.usecase.user.JoinUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.GoogleAuth
import io.tuttut.presentation.util.LinkUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserAndSaveGardenIdUseCase: GetUserAndSaveGardenIdUseCase,
    private val joinUseCase: JoinUseCase,
    private val googleAuth: GoogleAuth,
    private val linkUtil: LinkUtil,
) : BaseViewModel() {
    private val _uiState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val uiState: State<LoginUiState> get() =  _uiState

    fun onLogin(context: Context) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            googleAuth.login(context)
                .onSuccess { joinRequest ->
                    checkUserExist(joinRequest)
                }
                .onFailure {
                    // 구글 로그인 실패
                    resetUiState()
                }
        }
    }

    private suspend fun checkUserExist(joinRequest: JoinRequest) =
        getUserAndSaveGardenIdUseCase(joinRequest.id)
            .onSuccess {
                // 기존 유저 존재 -> 메인 화면 이동
            }
            .onFailure { t ->
                when (t) {
                    is ExceptionBoundary.DataNotFound -> {
                        // 유저 없음 -> 가입 위해 policySheet 띄움
                        _uiState.value = LoginUiState.PolicySheetState.Idle(joinRequest)
                    }
                    is ExceptionBoundary.GardenNotFound -> {
                        // gardenId 없음 -> 회원 가입 이동
                        resetUiState()
                    }
                    else -> {
                        // 유저 확인 실패
                        resetUiState()
                    }
                }
            }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }

    fun togglePolicyChecked(state: LoginUiState.PolicySheetState.Idle) {
        _uiState.value = state.copy(policyChecked = state.policyChecked.not())
    }

    fun togglePersonalChecked(state: LoginUiState.PolicySheetState.Idle) {
        _uiState.value = state.copy(personalChecked = state.personalChecked.not())
    }

    fun join(joinRequest: JoinRequest) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.PolicySheetState.Loading
            joinUseCase(joinRequest)
                .onSuccess {
                    // 회원 가입 이동
                    resetUiState()
                }
                .onFailure {
                    // 회원 가입 실패
                    _uiState.value = LoginUiState.PolicySheetState.Idle(joinRequest)
                }
        }
    }

    fun openBrowser(context: Context, url: String) = linkUtil.openBrowser(context, url)
}
