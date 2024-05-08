package io.tuttut.presentation.ui.screen.login.participate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.tuttut.data.model.dto.Garden
import io.tuttut.presentation.ui.component.SupportingTextType
import io.tuttut.presentation.ui.state.IEditTextState


sealed interface ParticipateUiState {
    data object Loading : ParticipateUiState
    data object Nothing : ParticipateUiState
}

class ParticipateTabState {
    var isNew by mutableStateOf(true)

    fun changeTab(state: Boolean) {
        isNew = state
    }
}

class ParticipateDialogState {
    var isOpen by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var garden = Garden()
    fun show(data: Garden) {
        garden = data
        isOpen = true
    }

    fun dismiss() {
        isOpen = false
        isLoading = false
    }

    fun changeLoadState(state: Boolean) {
        isLoading = state
    }
}

class ParticipateCodeState: IEditTextState {
    override var typedText by mutableStateOf("")
    var supportingText by mutableStateOf("")
    var supportingTextType by mutableStateOf(SupportingTextType.NONE)
    override fun typeText(text: String) {
        if (text.length <= 6) {
            typedText = text
            if (supportingTextType == SupportingTextType.ERROR) {
                supportingText = ""
                supportingTextType = SupportingTextType.NONE
            }
        }
    }

    override fun resetText() {
        typedText = ""
    }

    fun showNotFoundError() {
        supportingText = "텃밭 코드를 다시 확인해주세요"
        supportingTextType = SupportingTextType.ERROR
    }
}