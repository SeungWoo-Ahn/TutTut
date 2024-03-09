package io.tuttut.presentation.ui.screen.login

import io.tuttut.presentation.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {

    fun onLogin(tryLogin: () -> Unit) {
        tryLogin()
    }
}