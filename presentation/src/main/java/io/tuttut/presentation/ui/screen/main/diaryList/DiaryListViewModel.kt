package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.model.diary.DiaryWithAuthor
import io.tuttut.domain.usecase.diary.DeleteDiaryUseCase
import io.tuttut.domain.usecase.diary.GetDiaryListFlowUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toListItemUiModel
import io.tuttut.presentation.model.ToastModel
import io.tuttut.presentation.navigation.MainScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val toastModel: ToastModel,
    getDiaryListFlowUseCase: GetDiaryListFlowUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val cropsId = savedStateHandle.toRoute<MainScreen.DiaryList>().cropsId

    val uiState: StateFlow<DiaryListUiState> =
        getDiaryListFlowUseCase(cropsId)
            .map { list -> list.map(DiaryWithAuthor::toListItemUiModel) }
            .map(DiaryListUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DiaryListUiState.Loading
            )

    var sheetState by mutableStateOf<DiaryListSheetState>(DiaryListSheetState.Idle)
        private set

    fun showDeleteSheet(id: String) {
        sheetState = DiaryListSheetState.ShowDeleteSheet(id)
    }

    fun showReportSheet() {
        sheetState = DiaryListSheetState.ShowReportSheet
    }

    fun dismissSheet() {
        sheetState = DiaryListSheetState.Idle
    }

    fun onDelete() {
        val id = (sheetState as DiaryListSheetState.ShowDeleteSheet).id
        viewModelScope.launch {
            deleteDiaryUseCase(id)
                .onFailure {
                    toastModel.showToast("삭제에 실패했어요")
                }
            sheetState = DiaryListSheetState.Idle
        }
    }

    fun onReport(reason: String) {
        toastModel.showToast("${reason}로 신고했어요")
    }
}