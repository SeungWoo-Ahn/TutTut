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
import io.tuttut.data.repository.AuthRepository
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

    fun handleLoginResult(result: ActivityResult ,onNext: () -> Unit, moveMain: () -> Unit) {
        if (result.resultCode == RESULT_OK) {
            viewModelScope.launch {
                val singInResult = authClient.signInWithIntent(result.data ?: return@launch)
                val isNewUser = authRepo.checkIsNewUser(singInResult.data!!.userId)
                _uiState.value = Nothing
                if (isNewUser is Response.Success) {
                    if (isNewUser.data) onNext()
                    else moveMain()
                }
            }
        } else {
            _uiState.value = Nothing
        }
    }
}