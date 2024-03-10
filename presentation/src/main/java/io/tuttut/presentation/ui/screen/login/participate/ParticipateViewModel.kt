package io.tuttut.presentation.ui.screen.login.participate

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Response
import io.tuttut.presentation.ui.screen.login.participate.ParticipateUiState.*
import io.tuttut.data.repository.AuthRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.component.SupportingTextType
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateViewModel @Inject constructor(
    val authRepo: AuthRepository,
) : BaseViewModel()  {
    private val _uiState = mutableStateOf<ParticipateUiState>(Nothing)
    val uiState: State<ParticipateUiState> = _uiState

    private val _isNew = mutableStateOf(true)
    val isNew: State<Boolean> = _isNew

    private val _typedName = mutableStateOf("")
    val typedName: State<String> = _typedName

    private val _typedCode = mutableStateOf("")
    val typedCode: State<String> = _typedCode

    var codeSupportingText by mutableStateOf("")
    var supportingTextType by mutableStateOf(SupportingTextType.NONE)

    fun typeName(text: String) {
        if (text.length <= 10) {
            _typedName.value = text
        }
    }

    fun typeCode(text: String) {
        if (text.length <= 6) {
            _typedCode.value = text
            if (supportingTextType == SupportingTextType.ERROR) {
                supportingTextType = SupportingTextType.NONE
                codeSupportingText = ""
            }
        }
    }

    fun resetName() {
        _typedName.value = ""
    }

    fun resetCode() {
        _typedCode.value = ""
    }

    fun changeIsNew(state: Boolean) {
        resetName()
        resetCode()
        _isNew.value = state
    }

    fun onNext(hideKeyboard: () -> Unit, moveNext: () -> Unit) = viewModelScope.launch {
        hideKeyboard()
        _uiState.value = Loading
        if (isNew.value) {
            val userData = authClient.getSignedInUser()!!
            val result = authRepo.join(userData, typedName.value.trim())
            if (result is Response.Success) moveNext()
        } else {
            val result = authRepo.checkGardenExist(typedCode.value.trim())
            if (result is Response.Success) {
                if (result.data) {

                } else {
                    supportingTextType = SupportingTextType.ERROR
                    codeSupportingText = "텃밭 코드를 다시 확인해주세요"
                }
            }
        }
        _uiState.value = Nothing
    }
}