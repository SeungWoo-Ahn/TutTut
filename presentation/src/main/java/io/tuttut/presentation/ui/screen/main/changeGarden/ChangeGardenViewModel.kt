package io.tuttut.presentation.ui.screen.main.changeGarden

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeGardenViewModel @Inject constructor(
    private val gardenRepo: GardenRepository
) : BaseViewModel() {
    private val currentGarden = gardenRepo.currentGarden.value

    private val _uiState = MutableStateFlow<ChangeGardenUiState>(ChangeGardenUiState.Nothing)
    val uiState: StateFlow<ChangeGardenUiState> = _uiState

    private val _typedName = MutableStateFlow(currentGarden.name)
    val typedName: StateFlow<String> = _typedName

    fun typeGardenName(text: String) {
        if (text.length <= 10) {
            _typedName.value = text
        }
    }

    fun resetGardenName() {
        _typedName.value = ""
    }

    fun onSubmit(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            gardenRepo.updateGardenInfo(currentGarden.copy(name = typedName.value.trim())).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("변경에 실패했어요", null)
                    Result.Loading -> _uiState.value = ChangeGardenUiState.Loading
                    is Result.Success -> {
                        moveBack()
                        onShowSnackBar("텃밭 정보를 변경했어요", null)
                    }
                    else -> {}
                }
                _uiState.value = ChangeGardenUiState.Nothing
            }
        }
    }
}