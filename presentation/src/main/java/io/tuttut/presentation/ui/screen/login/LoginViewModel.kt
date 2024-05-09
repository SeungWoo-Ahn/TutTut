package io.tuttut.presentation.ui.screen.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.context.UserData
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.screen.login.LoginUiState.*
import io.tuttut.presentation.util.LinkUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val prefs: PreferenceUtil,
    private val linkUtil: LinkUtil,
) : BaseViewModel() {
    var uiState by mutableStateOf<LoginUiState>(Nothing)
    val policySheetState = PolicySheetState()
    private val _userData = MutableStateFlow(UserData())

    fun onLogin(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        uiState = Loading
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT) return
        viewModelScope.launch {
            val intentSender = authClient.signIn()
            intentSender?.let {
                val request = IntentSenderRequest.Builder(it).build()
                launcher.launch(request)
            }
        }
    }

    fun handleLoginResult(
        result: ActivityResult,
        onNext: () -> Unit,
        moveMain: () -> Unit,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        if (result.resultCode != RESULT_OK) {
            uiState = Nothing
            return
        }
        viewModelScope.launch {
            val userData = authClient.signInWithIntent(result.data) ?: return@launch
            _userData.value = userData
            authRepo.getUserResult(userData.userId).collect {
                when(it) {
                    Result.Loading -> uiState = Loading
                    Result.NotFound -> policySheetState.show()
                    is Result.Error -> onShowSnackBar("회원 확인에 실패했어요", null)
                    is Result.Success -> {
                        if (it.data.gardenId.isEmpty()) {
                            onNext()
                        } else {
                            it.data.apply {
                                prefs.gardenId = gardenId
                                prefs.userId = id
                            }
                            moveMain()
                        }
                    }
                }
                uiState = Nothing
            }
        }
    }

    fun openBrowser(context: Context, url: String) = linkUtil.openBrowser(context, url)

    fun join(onShowSnackBar: suspend (String, String?) -> Boolean) {
        policySheetState.onContinue()
        viewModelScope.launch {
            authRepo.join(_userData.value).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("가입에 실패했어요", null)
                    is Result.Success -> policySheetState.dismiss()
                    else -> {}
                }
                policySheetState.toNothing()
            }
        }
    }
}