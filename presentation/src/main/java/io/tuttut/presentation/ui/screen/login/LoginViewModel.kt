package io.tuttut.presentation.ui.screen.login

import android.app.Activity.RESULT_OK
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.screen.login.LoginUiState.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : BaseViewModel() {
    private val _uiState = mutableStateOf<LoginUiState>(Nothing)
    val uiState: State<LoginUiState> = _uiState

    fun onLogin(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        _uiState.value = Loading
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            viewModelScope.launch {
                val intentSender = authClient.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        intentSender ?: return@launch
                    ).build()
                )
            }
        }
    }

    fun handleLoginResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            viewModelScope.launch {
                val singInResult = authClient.signInWithIntent(result.data ?: return@launch)
                authRepo.getUserInfo(singInResult.data!!.userId).collect {
                    when(it) {
                        is Result.Success -> _uiState.value = Success
                        is Result.Error -> _uiState.value = Error
                        Result.Loading -> _uiState.value = Loading
                        Result.NotFound -> _uiState.value = NeedJoin
                    }
                }
            }
        } else {
            _uiState.value = Nothing
        }
    }
}