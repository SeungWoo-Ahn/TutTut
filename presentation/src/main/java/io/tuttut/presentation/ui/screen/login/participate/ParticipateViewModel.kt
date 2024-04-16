package io.tuttut.presentation.ui.screen.login.participate

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.response.Result
import io.tuttut.presentation.ui.screen.login.participate.ParticipateUiState.*
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.component.SupportingTextType
import io.tuttut.presentation.util.getCurrentDate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateViewModel @Inject constructor(
    private val gardenRepo: GardenRepository,
    private val prefs: PreferenceUtil
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

    fun onNext(hideKeyboard: () -> Unit, moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            hideKeyboard()
            if (isNew.value) createGarden(moveNext, onShowSnackBar)
            else checkGardenExist(onShowSnackBar)
        }
    }

    private suspend fun createGarden(moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val userId = authClient.getSignedInUser()?.userId ?: return
        gardenRepo.createGarden(userId, typedName.value.trim(), getCurrentDate()).collect {
            when (it) {
                Result.Loading -> _uiState.value = Loading
                is Result.Error -> onShowSnackBar("텃밭 생성에 실패했어요", null)
                is Result.Success -> {
                    prefs.gardenId = it.data
                    moveNext()
                }
                else -> {}
            }
            _uiState.value = Nothing
        }
    }

    private suspend fun checkGardenExist(onShowSnackBar: suspend (String, String?) -> Boolean) {
        gardenRepo.checkGardenExist(typedCode.value.trim()).collect {
            when (it) {
                Result.Loading -> _uiState.value = Loading
                Result.NotFound -> {
                    supportingTextType = SupportingTextType.ERROR
                    codeSupportingText = "텃밭 코드를 다시 확인해주세요"
                }
                is Result.Error -> onShowSnackBar("텃밭 검색에 실패했어요", null)
                is Result.Success -> {
                    dialogState = dialogState.copy(
                        isOpen = true,
                        content = it.data.first()
                    )
                }
            }
            _uiState.value = Nothing
        }
    }

    fun joinGarden(moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            val userId = authClient.getSignedInUser()?.userId ?: return@launch
            gardenRepo.joinGarden(userId, dialogState.content.id).collect {
                when (it) {
                    Result.Loading -> dialogState = dialogState.copy(isLoading = true)
                    is Result.Error -> onShowSnackBar("텃밭 참여에 실패했어요", null)
                    is Result.Success -> {
                        prefs.gardenId = it.data
                        moveNext()
                    }
                    else -> {}
                }
                dialogState = dialogState.copy(isOpen = false, isLoading = false)
            }
        }
    }
}