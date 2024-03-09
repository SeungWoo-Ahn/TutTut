package io.tuttut.presentation.ui.screen.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.SignInResult
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {
    private val _uiState = mutableStateOf<LoginUiState>(LoginUiState.Nothing)
    val uiState: State<LoginUiState> = _uiState

    fun onLogin(tryLogin: () -> Unit) {
        _uiState.value = LoginUiState.Loading
        tryLogin()
    }

    fun handleLoginResult(result: SignInResult) {

    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Nothing
    }
}