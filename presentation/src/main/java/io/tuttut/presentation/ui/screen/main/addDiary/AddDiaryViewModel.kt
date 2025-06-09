package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.network.model.Diary
import io.tuttut.data.network.model.StorageImage
import io.tuttut.data.network.model.toStorageImage
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.util.ImageUtil
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val storageRepo: StorageRepository,
    private val diaryRepo: DiaryRepository,
    private val imageUtil: ImageUtil,
    private val diaryModel: DiaryModel,
    private val pref: PreferenceUtil,
    cropsModel: CropsModel,
) : BaseViewModel() {
    private val crops = cropsModel.observedCrops.value
    private val diary = diaryModel.observedDiary.value
    val editMode = diaryModel.diaryEditMode.value

    private val _uiState = MutableStateFlow<AddDiaryUiState>(AddDiaryUiState.Nothing)
    val uiState: StateFlow<AddDiaryUiState> = _uiState

    private val _imageList = MutableStateFlow(diary.imgUrlList)
    val imageList: StateFlow<List<StorageImage>> = _imageList

    private val _originImageList = diary.imgUrlList.toList()

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
            val optimizedFile = imageUtil.getOptimizedFile(uri, MAX_WIDTH, MAX_HEIGHT) ?: continue
            updatedList.add(optimizedFile.toStorageImage())
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
        for (image in _originImageList) {
            if (!successUrls.contains(image.url)) {
                deleteImage(image.name)
            }
        }
    }

    private suspend fun editDiary(moveBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        val content = typedContent.value.trim()
        val successImages = uploadInputImages()
        withContext(Dispatchers.IO) {
            deleteImages(successImages)
        }
        val diary = diary.copy(
            content = content,
            imgUrlList = successImages
        )
        diaryRepo.updateDiary(pref.gardenId, diary).collect {
            when (it) {
                is Result.Error -> onShowSnackBar("일지 수정에 실패했어요", null)
                is Result.Success -> {
                    diaryModel.observeDiary(diary)
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
            authorId = pref.userId,
            content = content,
            created = getCurrentDateTime(),
            imgUrlList = successImages
        )
        diaryRepo.addDiary(pref.gardenId, diary).collect {
            when (it) {
                is Result.Error -> onShowSnackBar("일지 추가에 실패했어요", null)
                is Result.Success -> {
                    diaryModel.observeDiary(diary.copy(id = it.data))
                    moveDiaryDetail()
                    onShowSnackBar("일지를 추가했어요", null)
                }
                else -> {}
            }
            _uiState.value = AddDiaryUiState.Nothing
        }
    }

    companion object {
        private const val MAX_WIDTH = 600
        private const val MAX_HEIGHT = 600
    }
}