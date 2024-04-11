package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.auth.AuthRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.util.ImageUtil
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val storageRepo: StorageRepository,
    private val diaryRepo: DiaryRepository,
    authRepo: AuthRepository,
    private val imageUtil: ImageUtil,
    cropsModel: CropsModel,
) : BaseViewModel() {
    private val user = authRepo.currentUser.value
    private val crops = cropsModel.observedCrops.value
    private val diary = cropsModel.observedDiary.value
    val editMode = cropsModel.diaryEditMode.value

    private val _uiState = MutableStateFlow<AddDiaryUiState>(AddDiaryUiState.Nothing)
    val uiState: StateFlow<AddDiaryUiState> = _uiState

    private val _imageList = MutableStateFlow(diary.imgUrlList)
    val imageList: StateFlow<List<StorageImage>> = _imageList

    private val _typedContent = MutableStateFlow(diary.content)
    val typedContent: StateFlow<String> = _typedContent

    fun addImages(
        launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        if (imageList.value.size >= 3) {
            viewModelScope.launch { onShowSnackBar("세 장까지 선택 가능해요", null) }
            return
        }
        launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun handleImages(uriList: List<Uri>) {
        val updatedList = imageList.value.toMutableList()
        updatedList.addAll(uriList.take(3 - updatedList.size).map { StorageImage(imageUtil.getOptimizedUri(it) ?: "", "") })
        _imageList.value = updatedList
    }

    fun deleteImage(index: Int) {
        val updatedList = imageList.value.filterIndexed { idx, _ -> idx != index }.toList()
        _imageList.value = updatedList
    }

    fun typeContent(text: String) {
        _typedContent.value = text
    }

    fun onButton(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            _uiState.value = AddDiaryUiState.Loading
            if (editMode) editDiary(moveBack, onShowSnackBar)
            else addDiary(moveBack, onShowSnackBar)
        }
    }

    private fun optimizeUri(uriStr: String): Uri? {
        val optimizedUriPath = imageUtil.getOptimizedUri(uriStr.toUri()) ?: return null
        return Uri.fromFile(File(optimizedUriPath))
    }

    private suspend fun uploadImage(name: String, uri: Uri): String? {
        return storageRepo.uploadDiaryImage(name, uri).firstOrNull()
    }

    private suspend fun uploadInputImages(): List<StorageImage> {
        val inputImages = imageList.value.toList()
        val successImages = mutableListOf<StorageImage>()
        for ((idx, image) in inputImages.withIndex()) {
            if (image.url.contains("https")) {
                successImages.add(image)
            } else {
                val optimizedUri = optimizeUri(image.url) ?: continue
                val imageName = "${getCurrentDateTime()}_$idx"
                val downloadUrl = uploadImage(imageName, optimizedUri) ?: continue
                successImages.add(StorageImage(downloadUrl, imageName))
            }
        }
        return successImages.toList()
    }

    private suspend fun editDiary(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {

    }

    private suspend fun addDiary(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val successImages = uploadInputImages()
        val diary = Diary(
            cropsId = crops.id,
            authorId = user.id,
            content = typedContent.value.trim(),
            created = getCurrentDateTime(),
            imgUrlList = successImages
        )
        diaryRepo.addDiary(user.gardenId, diary).collect {
            when (it) {
                is Result.Error -> {
                    moveBack()
                }
                is Result.Success -> {
                    moveBack()
                    onShowSnackBar("일지를 추가했어요", null)
                }
                else -> {}
            }
            _uiState.value = AddDiaryUiState.Nothing
        }
    }
}