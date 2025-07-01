package io.tuttut.presentation.ui.screen.main.changeGarden

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.garden.GetGardenUseCase
import io.tuttut.domain.usecase.garden.UpdateGardenUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.state.TextFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeGardenViewModel @Inject constructor(
    private val updateGardenUseCase: UpdateGardenUseCase,
    private val getGardenUseCase: GetGardenUseCase,
) : BaseViewModel() {
    var uiState by mutableStateOf<ChangeGardenUiState>(ChangeGardenUiState.Idle)
        private set

    val nameState = TextFieldState(10)

    init {
        viewModelScope.launch {
            getGardenUseCase()
                .onSuccess { garden ->
                    nameState.typeText(garden.name)
                }
        }
    }

    fun onSubmit(moveBack: () -> Unit) {
        viewModelScope.launch {
            updateGardenUseCase(nameState.getTrimmedText())
                .onSuccess {
                    moveBack()
                    // 텃밭 정보를 변경했어요
                }
                .onFailure {
                    // 변경에 실패했어요
                }
            uiState = ChangeGardenUiState.Idle
        }
    }
}