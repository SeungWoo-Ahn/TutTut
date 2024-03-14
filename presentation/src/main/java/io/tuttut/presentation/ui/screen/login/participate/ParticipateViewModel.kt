package io.tuttut.presentation.ui.screen.login.participate

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.response.Result
import io.tuttut.presentation.ui.screen.login.participate.ParticipateUiState.*
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.component.SupportingTextType
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val gardenRepo: GardenRepository
) : BaseViewModel()  {
    private val _uiState = mutableStateOf<ParticipateUiState>(Nothing)
    val uiState: State<ParticipateUiState> = _uiState

    var dialogState by mutableStateOf(ParticipateDialogUiState())

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

    fun onNext(hideKeyboard: () -> Unit, moveNext: () -> Unit) {
        viewModelScope.launch {
            hideKeyboard()
            if (isNew.value) join(moveNext)
            else checkGardenExist()
        }
    }

    private suspend fun join(moveNext: () -> Unit) {
        val userData = authClient.getSignedInUser()!!
        authRepo.join(userData, typedName.value.trim()).collect {
            when (it) {
                is Result.Success -> moveNext()
                Result.Loading -> _uiState.value = Loading
                else -> TODO("에러 핸들링")
            }
            _uiState.value = Nothing
        }
    }

    private suspend fun checkGardenExist() {
        gardenRepo.checkGardenExist(typedCode.value.trim()).collect {
            when (it) {
                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        dialogState = dialogState.copy(
                            isOpen = true,
                            content = it.data.first()
                        )
                    } else {
                        supportingTextType = SupportingTextType.ERROR
                        codeSupportingText = "텃밭 코드를 다시 확인해주세요"
                    }
                }
                Result.Loading -> _uiState.value = Loading
                else -> TODO("에러 핸들링")
            }
            _uiState.value = Nothing
        }
    }

    fun onConfirmParticipate(moveNext: () -> Unit) {
        viewModelScope.launch {
            val userData = authClient.getSignedInUser()!!
            authRepo.joinOtherGarden(userData, dialogState.content).collect {
                when (it) {
                    is Result.Success -> moveNext()
                    Result.Loading -> dialogState = dialogState.copy(isLoading = true)
                    else -> TODO("에러 핸들링")
                }
            }
            dialogState = dialogState.copy(isOpen = false, isLoading = false)
        }
    }
}