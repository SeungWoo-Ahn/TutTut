package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.util.ImageUtil
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val storageRepo: StorageRepository,
    private val diaryRepo: DiaryRepository,
    authRepo: AuthRepository,
    private val imageUtil: ImageUtil,
    private val cropsModel: CropsModel,
    private val diaryModel: DiaryModel
) : BaseViewModel() {
    private val user = authRepo.currentUser.value
    private val crops = cropsModel.observedCrops.value
    private val diary = diaryModel.observedDiary.value
    val editMode = diaryModel.diaryEditMode.value

    private val _uiState = MutableStateFlow<AddDiaryUiState>(AddDiaryUiState.Nothing)
    val uiState: StateFlow<AddDiaryUiState> = _uiState

    private val _imageList = MutableStateFlow(diary.imgUrlList)
    val imageList: StateFlow<List<StorageImage>> = _imageList

    private val _originImageList = MutableStateFlow(diary.imgUrlList)

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
        for (uri in uriList) {
            val optimizedFile = imageUtil.getOptimizedFile(uri) ?: continue
            updatedList.add(StorageImage(optimizedFile.absolutePath, optimizedFile.name))
            if (updatedList.size == 3) break
        }
        _imageList.value = updatedList
    }

    fun deleteImage(index: Int) {
        val updatedList = imageList.value.filterIndexed { idx, _ -> idx != index }.toList()
        _imageList.value = updatedList
    }

    fun typeContent(text: String) {
        _typedContent.value = text
    }

    fun onButton(moveBack: () -> Unit, moveDiaryDetail: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            _uiState.value = AddDiaryUiState.Loading
            if (editMode) editDiary(moveBack, onShowSnackBar)
            else addDiary(moveDiaryDetail, onShowSnackBar)
        }
    }

    private suspend fun uploadImage(image: StorageImage): String? {
        return storageRepo.uploadDiaryImage(image.name, imageUtil.getUriFromPath(image.url)).firstOrNull()
    }

    private suspend fun uploadInputImages(): List<StorageImage> {
        val inputImages = imageList.value.toList()
        val successImages = mutableListOf<StorageImage>()
        for (image in inputImages) {
            if (image.url.contains("https")) {
                successImages.add(image)
            } else {
                val downloadUrl = uploadImage(image) ?: continue
                successImages.add(image.copy(url = downloadUrl))
            }
        }
        return successImages.toList()
    }

    private suspend fun deleteImage(name: String): Boolean {
        return storageRepo.deleteDiaryImage(name).first()
    }

    private suspend fun deleteImages(successImages: List<StorageImage>) {
        val successUrls = successImages.map { it.url }
        for (image in _originImageList.value) {
            if (!successUrls.contains(image.url)) {
                deleteImage(image.name)
            }
        }
    }

    private suspend fun editDiary(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val content = typedContent.value.trim()
        val successImages = uploadInputImages()
        with(successImages) { deleteImages(this) }
        val diary = diary.copy(
            content = content,
            imgUrlList = successImages
        )
        diaryRepo.updateDiary(user.gardenId, diary).collect {
            when (it) {
                is Result.Error -> onShowSnackBar("일지 수정에 실패했어요", null)
                is Result.Success -> {
                    diaryModel.observeDiary(diary)
                    cropsModel.refreshCropsList()
                    moveBack()
                    onShowSnackBar("일지를 수정했어요", null)
                }
                else -> {}
            }
            _uiState.value = AddDiaryUiState.Nothing
        }
    }

    private suspend fun addDiary(moveDiaryDetail: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val content = typedContent.value.trim()
        val successImages = uploadInputImages()
        val diary = Diary(
            cropsId = crops.id,
            authorId = user.id,
            content = content,
            created = getCurrentDateTime(),
            imgUrlList = successImages
        )
        diaryRepo.addDiary(user.gardenId, diary).collect {
            when (it) {
                is Result.Error -> onShowSnackBar("일지 추가에 실패했어요", null)
                is Result.Success -> {
                    diaryModel.observeDiary(diary.copy(id = it.data))
                    cropsModel.refreshCropsList()
                    moveDiaryDetail()
                    onShowSnackBar("일지를 추가했어요", null)
                }
                else -> {}
            }
            _uiState.value = AddDiaryUiState.Nothing
        }
    }
}