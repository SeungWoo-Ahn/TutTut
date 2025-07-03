package io.tuttut.presentation.ui.screen.main.changeProfile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.usecase.user.GetCurrentUserUseCase
import io.tuttut.domain.usecase.user.UpdateUserUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.ui.state.TextFieldState
import io.tuttut.presentation.util.ImageUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val imageUtil: ImageUtil
) : BaseViewModel() {
    var uiState by mutableStateOf<ChangeProfileUiState>(ChangeProfileUiState.Idle)
        private set

    var profileImage by mutableStateOf<ImageSource?>(null)
        private set

    val nameState = TextFieldState(10)

    init {
        setUserData()
    }

    private fun setUserData() {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .onSuccess { user ->
                    profileImage = user.profile
                    nameState.typeText(user.name)
                }
        }
    }

    fun onPhotoPickerResult(uri: Uri?) {
        if (uri != null) {
            viewModelScope.launch {
                imageUtil.compressUriToFile(uri, MAX_WIDTH, MAX_HEIGHT)
                    .onSuccess { file ->
                        profileImage = ImageSource.Local(file)
                    }
                    .onFailure {
                        // 이미지 변환에 실패했어요
                    }
            }
        }
    }

    fun validate(): Boolean = profileImage != null && nameState.isValidate()


    fun onSubmit(moveBack: () -> Unit) {
        viewModelScope.launch {
            uiState = ChangeProfileUiState.Loading
            updateUserUseCase(nameState.getTrimmedText(), profileImage!!)
                .onSuccess {
                    moveBack()
                    // 프로필을 변경했어요
                }
                .onFailure {
                    uiState = ChangeProfileUiState.Idle
                    // 프로필 변경에 실패했어요
                }
        }
    }

    companion object {
        private const val MAX_WIDTH = 200
        private const val MAX_HEIGHT = 200
    }
}