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
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.screen.login.LoginUiState.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val prefs: PreferenceUtil
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

    fun handleLoginResult(result: ActivityResult, onNext: () -> Unit, moveMain: () -> Unit) {
        if (result.resultCode == RESULT_OK) {
            viewModelScope.launch {
                val singInResult = authClient.signInWithIntent(result.data ?: return@launch)
                authRepo.getUserResult(singInResult.data!!.userId).collect {
                    when(it) {
                        is Result.Success -> {
                            val gardenId = it.data.gardenId
                            if (gardenId.isEmpty()) onNext()
                            else {
                                prefs.gardenId = it.data.gardenId
                                moveMain()
                            }
                        }
                        is Result.Error -> TODO("에러 핸들링")
                        Result.Loading -> _uiState.value = Loading
                        Result.NotFound -> onNext()
                    }
                    _uiState.value = Nothing
                }
            }
        } else {
            _uiState.value = Nothing
        }
    }
}