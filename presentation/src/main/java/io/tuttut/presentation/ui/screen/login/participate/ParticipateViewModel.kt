package io.tuttut.presentation.ui.screen.login.participate

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
import io.tuttut.presentation.ui.state.EditTextState
import io.tuttut.presentation.util.getCurrentDate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateViewModel @Inject constructor(
    private val gardenRepo: GardenRepository,
    private val prefs: PreferenceUtil
) : BaseViewModel()  {
    private var _uiState by mutableStateOf<ParticipateUiState>(Nothing)
    val uiState = _uiState
    val tabState = ParticipateTabState()
    val nameState = EditTextState(maxLength = 10)
    val codeState = ParticipateCodeState()
    val dialogState = ParticipateDialogState()

    fun onNext(hideKeyboard: () -> Unit, moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            hideKeyboard()
            if (tabState.isNew) createGarden(moveNext, onShowSnackBar)
            else checkGardenExist(onShowSnackBar)
        }
    }

    private suspend fun createGarden(moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val userId = authClient.getSignedInUser()?.userId ?: return
        gardenRepo.createGarden(userId, nameState.typedText.trim(), getCurrentDate()).collect {
            when (it) {
                Result.Loading -> _uiState = Loading
                is Result.Error -> onShowSnackBar("텃밭 생성에 실패했어요", null)
                is Result.Success -> {
                    prefs.gardenId = it.data
                    moveNext()
                }
                else -> {}
            }
            _uiState = Nothing
        }
    }

    private suspend fun checkGardenExist(onShowSnackBar: suspend (String, String?) -> Boolean) {
        gardenRepo.checkGardenExist(codeState.typedText.trim()).collect {
            when (it) {
                Result.Loading -> _uiState = Loading
                Result.NotFound -> codeState.showNotFoundError()
                is Result.Error -> onShowSnackBar("텃밭 검색에 실패했어요", null)
                is Result.Success -> dialogState.show(it.data.first())
            }
            _uiState = Nothing
        }
    }

    fun joinGarden(moveNext: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            val userId = authClient.getSignedInUser()?.userId ?: return@launch
            gardenRepo.joinGarden(userId, dialogState.garden.id).collect {
                when (it) {
                    Result.Loading -> dialogState.changeLoadState(true)
                    is Result.Error -> onShowSnackBar("텃밭 참여에 실패했어요", null)
                    is Result.Success -> {
                        dialogState.dismiss()
                        prefs.gardenId = it.data
                        moveNext()
                    }
                    else -> {}
                }
            }
        }
    }
}