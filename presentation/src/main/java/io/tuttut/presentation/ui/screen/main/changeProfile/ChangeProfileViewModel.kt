package io.tuttut.presentation.ui.screen.main.changeProfile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.constant.DEFAULT_IMAGE_NAME
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.dto.toStorageImage
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.UserModel
import io.tuttut.presentation.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangeProfileViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val storageRepo: StorageRepository,
    private val userModel: UserModel,
    private val imageUtil: ImageUtil
) : BaseViewModel() {
    private val currentUser = authRepo.currentUser.value
    private val _originUserInfo = currentUser.copy()

    private val _uiState = MutableStateFlow<ChangeProfileUiState>(ChangeProfileUiState.Nothing)
    val uiState: StateFlow<ChangeProfileUiState> = _uiState

    private val _profileImage = MutableStateFlow(currentUser.profile)
    val profileImage: StateFlow<StorageImage> = _profileImage

    private val _typedName = MutableStateFlow(currentUser.name)
    val typedName: StateFlow<String> = _typedName

    fun onChangeImage(launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
        launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun handleImage(uri: Uri?) {
        if (uri == null) return
        val optimizedFile = imageUtil.getOptimizedFile(uri) ?: return
        _profileImage.value = optimizedFile.toStorageImage()
    }

    fun typeName(text: String) {
        if (text.length <= 10) {
            _typedName.value = text
        }
    }

    fun resetName() {
        _typedName.value = ""
    }

    private suspend fun uploadInputImage(): StorageImage {
        val inputImage = profileImage.value
        if (inputImage.name == DEFAULT_IMAGE_NAME) return inputImage
        val downloadUrl = storageRepo.uploadProfileImage(
            name =  inputImage.name,
            uri = imageUtil.getUriFromPath(inputImage.url)
        ).firstOrNull() ?: return inputImage
        return inputImage.copy(url = downloadUrl)
    }

    private suspend fun deleteOriginImage(): Boolean {
        val originProfile = _originUserInfo.profile
        if (originProfile.url.contains(GOOGLE_PROFILE_KEY)) return true
        return storageRepo.deleteDiaryImage(originProfile.name).first()
    }

    fun onSubmit(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val profileChanged = profileImage.value.url != _originUserInfo.profile.url
        val nameChanged = typedName.value.trim() != _originUserInfo.name
        if (!profileChanged && !nameChanged) {
            moveBack()
            return
        }
        viewModelScope.launch {
            _uiState.value = ChangeProfileUiState.Loading
            var successImage = _originUserInfo.profile
            if (profileChanged) {
                successImage = uploadInputImage()
                withContext(Dispatchers.IO) {
                    deleteOriginImage()
                }
            }
            authRepo.updateUserInfo(
                currentUser.copy(
                    name = typedName.value.trim(),
                    profile = successImage
                )
            ).collect {
                when (it) {
                    is Result.Error -> onShowSnackBar("변경에 실패했어요", null)
                    is Result.Success -> {
                        userModel.refreshMember()
                        moveBack()
                        onShowSnackBar("프로필을 변경했어요", null)
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val GOOGLE_PROFILE_KEY = "googleusercontent"
    }
}