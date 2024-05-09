package io.tuttut.presentation.ui.screen.main.addDiary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.storage.StorageRepository
import io.tuttut.presentation.ui.screen.main.addDiary.AddDiaryUiState.*
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.EditTextState
import io.tuttut.presentation.util.ImageUtil
import io.tuttut.presentation.util.getCurrentDateTime
import kotlinx.coroutines.Dispatchers
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

    var uiState by mutableStateOf<AddDiaryUiState>(Nothing)
    val imageState = DiaryImageState(
        initList = diary.imgUrlList,
        maxSize = 3,
        scope = viewModelScope,
        imageUtil = imageUtil
    )
    val contentState = EditTextState(
        initText = diary.content,
        maxLength = 300
    )

    fun onButton(moveBack: () -> Unit, moveDiaryDetail: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            uiState = Loading
            if (editMode) editDiary(moveBack, onShowSnackBar)
            else addDiary(moveDiaryDetail, onShowSnackBar)
        }
    }

    private suspend fun uploadImage(image: StorageImage): String? {
        return storageRepo.uploadDiaryImage(image.name, imageUtil.getUriFromPath(image.url)).firstOrNull()
    }

    private suspend fun uploadInputImages(): List<StorageImage> {
        val successImages = mutableListOf<StorageImage>()
        for (image in imageState.getNeedUploadImageList()) {
            val downloadUrl = uploadImage(image) ?: continue
            successImages.add(image.copy(url = downloadUrl))
        }
        return successImages.toList()
    }

    private suspend fun deleteImages() {
        imageState.deletedImageList.forEach { name ->
            storageRepo.deleteDiaryImage(name).first()
        }
    }

    private suspend fun editDiary(
        moveBack: () -> Unit,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        val content = contentState.typedText.trim()
        val successImages = uploadInputImages()
        withContext(Dispatchers.IO) { deleteImages() }
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
            uiState = Nothing
        }
    }

    private suspend fun addDiary(
        moveDiaryDetail: () -> Unit,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        val content = contentState.typedText.trim()
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
            uiState = Nothing
        }
    }
}