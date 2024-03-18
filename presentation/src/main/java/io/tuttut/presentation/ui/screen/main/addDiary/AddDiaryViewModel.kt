package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    cropsModel: CropsModel
) : BaseViewModel() {
    private val diary = cropsModel.observedDiary.value
    val editMode = cropsModel.diaryEditMode.value

    private val _imageList = MutableStateFlow(diary.imgUrlList)
    val imageList: StateFlow<List<String>> = _imageList

    private val _typedContent = MutableStateFlow(diary.content)
    val typedContent: StateFlow<String> = _typedContent

    fun addImages(
        launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
        onShowSnackBar: suspend (String, String?) -> Boolean
    ) {
        if (imageList.value.size >= 3) {
            viewModelScope.launch {
                onShowSnackBar("세 장까지 선택 가능해요", null)
            }
        } else {
            launcher.launch(PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            ))

        }
    }

    fun handleImages(uriList: List<Uri>) {
        val origin = imageList.value.toMutableList()
        origin.addAll(uriList.take(3 - origin.size).map { it.toString() })
        _imageList.value = origin
    }

    fun deleteImage(index: Int) {
        val origin = imageList.value.filterIndexed { idx, _ -> idx != index }.toList()
        _imageList.value = origin
    }

    fun typeContent(text: String) {
        _typedContent.value = text
    }

    fun onButton(moveBack: () -> Unit) {

    }

}