package io.tuttut.presentation.ui.screen.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.exception.ExceptionBoundary
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.domain.usecase.user.GetUserAndSaveIdUseCase
import io.tuttut.domain.usecase.user.JoinUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.GoogleAuth
import io.tuttut.presentation.model.ToastModel
import io.tuttut.presentation.util.LinkUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserAndSaveIdUseCase: GetUserAndSaveIdUseCase,
    private val joinUseCase: JoinUseCase,
    private val googleAuth: GoogleAuth,
    private val linkUtil: LinkUtil,
    private val toastModel: ToastModel,
) : BaseViewModel() {
    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun onLogin(context: Context, moveMain: () -> Unit, moveParticipate: (String) -> Unit) {
        viewModelScope.launch {
            uiState = LoginUiState.Loading
            googleAuth.login(context)
                .onSuccess { joinRequest ->
                    checkUserExist(joinRequest, moveMain, moveParticipate)
                }
                .onFailure {
                    resetUiState()
                    toastModel.showToast("구글 로그인에 실패했어요")
                }
        }
    }

    private suspend fun checkUserExist(
        joinRequest: JoinRequest,
        moveMain: () -> Unit,
        moveParticipate: (String) -> Unit
    ) {
        getUserAndSaveIdUseCase(joinRequest.id)
            .onSuccess {
                moveMain()
            }
            .onFailure { t ->
                when (t) {
                    is ExceptionBoundary.DataNotFound -> {
                        uiState = LoginUiState.PolicySheetState.Idle(joinRequest)
                    }
                    is ExceptionBoundary.GardenNotFound -> {
                        moveParticipate(joinRequest.id)
                        resetUiState()
                    }
                    else -> {
                        resetUiState()
                        toastModel.showToast("회원 확인에 실패했어요")
                    }
                }
            }
    }


    fun resetUiState() {
        uiState = LoginUiState.Idle
    }

    fun togglePolicyChecked(state: LoginUiState.PolicySheetState.Idle) {
        uiState = state.copy(policyChecked = state.policyChecked.not())
    }

    fun togglePersonalChecked(state: LoginUiState.PolicySheetState.Idle) {
        uiState = state.copy(personalChecked = state.personalChecked.not())
    }

    fun join(joinRequest: JoinRequest, moveParticipate: (String) -> Unit) {
        viewModelScope.launch {
            uiState = LoginUiState.PolicySheetState.Loading
            joinUseCase(joinRequest)
                .onSuccess {
                    moveParticipate(joinRequest.id)
                    resetUiState()
                }
                .onFailure {
                    uiState = LoginUiState.PolicySheetState.Idle(joinRequest)
                    toastModel.showToast("회원 가입에 실패했어요")
                }
        }
    }

    fun openBrowser(context: Context, url: String) = linkUtil.openBrowser(context, url)
}
