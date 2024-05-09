package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.dto.toStorageImage
import io.tuttut.presentation.util.ImageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed interface AddDiaryUiState {
    data object Loading : AddDiaryUiState
    data object Nothing : AddDiaryUiState
}

fun AddDiaryUiState.isLoading() = this == AddDiaryUiState.Loading

class DiaryImageState(
    initList: List<StorageImage>,
    private val maxSize: Int,
    private val scope: CoroutineScope,
    private val imageUtil: ImageUtil
) {
    var imageList by mutableStateOf(initList)
    val deletedImageList = mutableListOf<String>()

    fun addImages(
        launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        if (imageList.size >= maxSize) {
            scope.launch { onShowSnackBar("세 장까지 선택 가능해요", null) }
            return
        }
        launcher.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    fun handleImages(uriList: List<Uri>) {
        val updatedList = imageList.toMutableList()
        for (uri in uriList) {
            imageUtil.getOptimizedFile(uri, MAX_WIDTH, MAX_HEIGHT)?.let { file ->
                updatedList.add(file.toStorageImage())
            }
            if (updatedList.size == maxSize) break
        }
        imageList = updatedList
    }

    fun deleteImage(index: Int) {
        if (imageList[index].url.contains(UPLOADED_KEY))
            deletedImageList.add(imageList[index].name)
        val updatedList = imageList.filterIndexed { i, _ -> i != index }.toList()
        imageList = updatedList
    }

    fun getNeedUploadImageList(): List<StorageImage> {
        return imageList.filter { !it.url.contains(UPLOADED_KEY) }.toList()
    }

    companion object {
        private const val MAX_WIDTH = 600
        private const val MAX_HEIGHT = 600
        private const val UPLOADED_KEY = "https"
    }
}