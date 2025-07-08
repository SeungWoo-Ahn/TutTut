package io.tuttut.presentation.ui.screen.login.participate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.garden.CreateGardenRequest
import io.tuttut.domain.model.garden.Garden
import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.usecase.garden.CreateGardenUseCase
import io.tuttut.domain.usecase.garden.GetGardenByCodeUseCase
import io.tuttut.domain.usecase.garden.JoinGardenUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.ToastModel
import io.tuttut.presentation.navigation.LoginScreen
import io.tuttut.presentation.ui.state.CodeTextFieldState
import io.tuttut.presentation.ui.state.TextFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateViewModel @Inject constructor(
    private val createGardenUseCase: CreateGardenUseCase,
    private val getGardenByCodeUseCase: GetGardenByCodeUseCase,
    private val joinGardenUseCase: JoinGardenUseCase,
    private val toastModel: ToastModel,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel()  {
    private val userId = savedStateHandle.toRoute<LoginScreen.Participate>().userId

    var uiState by mutableStateOf<ParticipateUiState>(ParticipateUiState.Idle)
        private set

    var tab by mutableStateOf(ParticipateTab.CREATE)
        private set

    val nameState = TextFieldState(10)
    val codeState = CodeTextFieldState()

    fun onTabChanged(selectedTab: ParticipateTab) {
        tab = selectedTab
    }

    fun validate(): Boolean {
        return when (tab) {
            ParticipateTab.CREATE -> nameState.isValidate()
            ParticipateTab.JOIN -> codeState.isValidate()
        }
    }

    fun onNext(moveWelcome: () -> Unit) {
        viewModelScope.launch {
            uiState = ParticipateUiState.Loading
            when (tab) {
                ParticipateTab.CREATE -> createGarden(moveWelcome)
                ParticipateTab.JOIN -> checkGardenExist()
            }
        }
    }

    private suspend fun createGarden(moveWelcome: () -> Unit) {
        val createGardenRequest = CreateGardenRequest(
            userId = userId,
            gardenName = nameState.getTrimmedText()
        )
        createGardenUseCase(createGardenRequest)
            .onSuccess {
                moveWelcome()
            }
            .onFailure {
                // 텃밭 생성 실패
                toastModel.showToast("${createGardenRequest.gardenName} 생성에 실패했어요")
            }
        resetUiState()
    }

    private suspend fun checkGardenExist() {
        val code = codeState.getTrimmedText()
        getGardenByCodeUseCase(code)
            .onSuccess { garden ->
                uiState = ParticipateUiState.DialogState.Idle(garden)
            }
            .onFailure {
                codeState.ifNotFound()
                resetUiState()
            }
    }

    fun resetUiState() {
        uiState = ParticipateUiState.Idle
    }

    fun joinGarden(garden: Garden, moveWelcome: () -> Unit) {
        viewModelScope.launch {
            uiState = ParticipateUiState.DialogState.Loading(garden)
            val credential = Credential(
                userId = userId,
                gardenId = garden.id
            )
            joinGardenUseCase(credential)
                .onSuccess {
                    resetUiState()
                    moveWelcome()
                }
                .onFailure {
                    // 텃밭 참여 실패
                    uiState = ParticipateUiState.DialogState.Idle(garden)
                    toastModel.showToast("${garden.name} 참여에 실패했어요")
                }
        }
    }
}