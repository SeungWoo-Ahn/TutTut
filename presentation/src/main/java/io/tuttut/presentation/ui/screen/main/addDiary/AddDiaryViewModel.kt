package io.tuttut.presentation.ui.screen.main.addDiary

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.domain.usecase.diary.AddDiaryUseCase
import io.tuttut.domain.usecase.diary.GetDiaryFlowUseCase
import io.tuttut.domain.usecase.diary.UpdateDiaryUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.navigation.MainScreen
import io.tuttut.presentation.ui.state.TextFieldState
import io.tuttut.presentation.util.ImageUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val addDiaryUseCase: AddDiaryUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
    private val getDiaryFlowUseCase: GetDiaryFlowUseCase,
    private val imageUtil: ImageUtil,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val purpose = savedStateHandle.toRoute<MainScreen.AddDiary>()
    val editMode = purpose.diaryId != null

    var uiState by mutableStateOf<AddDiaryUiState>(AddDiaryUiState.Idle)
        private set

    var imageList by mutableStateOf<List<ImageSource>>(emptyList())
        private set

    val contentState = TextFieldState(10_000)

    init {
        viewModelScope.launch {
            purpose.diaryId?.let { id ->
                setDiaryData(id)
            }
        }
    }

    private suspend fun setDiaryData(diaryId: String) {
        getDiaryFlowUseCase(diaryId)
            .first()
            .let { diary ->
                imageList = diary.imageList
                contentState.typeText(diary.content)
            }
    }

    fun onPhotoPickerResult(uriList: List<Uri>) {
        viewModelScope.launch {
            val compressedImageList = mutableListOf<ImageSource.Local>()
            for (uri in uriList) {
                imageUtil.compressUriToFile(uri, MAX_WIDTH, MAX_HEIGHT)
                    .onSuccess { file ->
                        compressedImageList.add(ImageSource.Local(file))
                    }
                if (imageList.size + compressedImageList.size == 3) break
            }
            imageList = imageList + compressedImageList
        }
    }

    fun deleteImage(image: ImageSource) {
        imageList = imageList - image
    }

    fun validate(): Boolean = contentState.isValidate()

    fun onButton(moveDiaryDetail: (String) -> Unit) {
        viewModelScope.launch {
            uiState = AddDiaryUiState.Loading
            purpose.cropsId?.let { cropsId ->
                addDiary(cropsId, moveDiaryDetail)
            }
            purpose.diaryId?.let { diaryId ->
                editDiary(diaryId, moveDiaryDetail)
            }
        }
    }

    private suspend fun addDiary(cropsId: String, moveDiaryDetail: (String) -> Unit) {
        addDiaryUseCase(cropsId, contentState.getTrimmedText(), imageList)
            .onSuccess { diaryId ->
                moveDiaryDetail(diaryId)
                // 일지를 추가했어요
            }
            .onFailure {
                uiState = AddDiaryUiState.Idle
                // 일지 추가에 실패했어요
            }
    }

    private suspend fun editDiary(diaryId: String, moveDiaryDetail: (String) -> Unit) {
        updateDiaryUseCase(diaryId, contentState.getTrimmedText(), imageList)
            .onSuccess {
                moveDiaryDetail(diaryId)
                // 일지를 수정했어요
            }
            .onFailure {
                uiState = AddDiaryUiState.Idle
                // 일지 수정에 실패했어요
            }
    }

    companion object {
        private const val MAX_WIDTH = 600
        private const val MAX_HEIGHT = 600
    }
}