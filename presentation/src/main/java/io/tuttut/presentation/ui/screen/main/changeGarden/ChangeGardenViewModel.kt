package io.tuttut.presentation.ui.screen.main.changeGarden

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.ui.screen.main.changeGarden.ChangeGardenUiState.*
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.state.EditTextState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeGardenViewModel @Inject constructor(
    private val gardenRepo: GardenRepository
) : BaseViewModel() {
    private val currentGarden = gardenRepo.currentGarden.value
    private val originName = currentGarden.name

    private var _uiState by mutableStateOf<ChangeGardenUiState>(Nothing)
    val uiState = _uiState
    val nameState = EditTextState(initText = currentGarden.name, maxLength = 10)

    fun onSubmit(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        if (nameState.typedText.trim() == originName) {
            moveBack()
            return
        }
        viewModelScope.launch {
            gardenRepo.updateGardenInfo(currentGarden.copy(name = nameState.typedText.trim())).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("변경에 실패했어요", null)
                    Result.Loading -> _uiState = Loading
                    is Result.Success -> {
                        moveBack()
                        onShowSnackBar("텃밭 정보를 변경했어요", null)
                    }
                    else -> {}
                }
                _uiState = Nothing
            }
        }
    }
}