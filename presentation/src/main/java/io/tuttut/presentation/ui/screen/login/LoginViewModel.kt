package io.tuttut.presentation.ui.screen.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.Response
import io.tuttut.data.repository.AuthRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.data.model.SignInResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : BaseViewModel() {
    private val _uiState = mutableStateOf<LoginUiState>(LoginUiState.Nothing)
    val uiState: State<LoginUiState> = _uiState

    fun onLogin(tryLogin: () -> Unit) {
        _uiState.value = LoginUiState.Loading
        tryLogin()
    }

    fun handleLoginResult(result: SignInResult, onNext: () -> Unit, moveMain: () -> Unit) = viewModelScope.launch {
        val isNewUser = authRepo.checkIsNewUser(result.data!!.userId)
        _uiState.value = LoginUiState.Nothing
        if (isNewUser is Response.Success) {
            if (isNewUser.data) onNext()
            else moveMain()
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Nothing
    }
}