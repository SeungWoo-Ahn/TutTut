package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.domain.usecase.crops.DeleteCropsUseCase
import io.tuttut.domain.usecase.crops.GetCropsFlowUseCase
import io.tuttut.domain.usecase.crops.HarvestCropsUseCase
import io.tuttut.domain.usecase.crops.WateringCropsUseCase
import io.tuttut.domain.usecase.cropsInfo.GetCropsRecipeFlowUseCase
import io.tuttut.domain.usecase.diary.GetDiaryListFlowUseCase
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.mapper.toDetailCropsUiModel
import io.tuttut.presentation.mapper.toDetailDiaryUiModel
import io.tuttut.presentation.model.WateringState
import io.tuttut.presentation.navigation.MainScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropsDetailViewModel @Inject constructor(
    private val wateringCropsUseCase: WateringCropsUseCase,
    private val harvestCropsUseCase: HarvestCropsUseCase,
    private val deleteCropsUseCase: DeleteCropsUseCase,
    getCropsFlowUseCase: GetCropsFlowUseCase,
    getDiaryListFlowUseCase: GetDiaryListFlowUseCase,
    getCropsRecipeFlowUseCase: GetCropsRecipeFlowUseCase,
    savedStateHandle: SavedStateHandle,
): BaseViewModel() {
    private val route = savedStateHandle.toRoute<MainScreen.CropsDetail>()

    val uiState: StateFlow<CropsDetailUiState> =
        combine(
            flow = getCropsFlowUseCase(route.cropsId),
            flow2 = getDiaryListFlowUseCase(route.cropsId).take(6),
            flow3 = getCropsRecipeFlowUseCase(route.cropsName).take(10),
        ) { crops, diaryList, recipeList ->
            CropsDetailUiState.Success(
                crops = crops.toDetailCropsUiModel(),
                diaryList = diaryList.map { it.toDetailDiaryUiModel() },
                recipeList = recipeList
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CropsDetailUiState.Loading
        )

    var showDeleteDialog by mutableStateOf(false)
        private set
    var showHarvestDialog by mutableStateOf(false)
        private set

    fun setDeleteDialogState(state: Boolean) {
        showDeleteDialog = state
    }

    fun setHarvestDialogState(state: Boolean) {
        showHarvestDialog = state
    }

    fun onWatering(wateringState: WateringState) {
        when (wateringState) {
            WateringState.IMPOSSIBLE -> {
                // 물 주기 간격을 설정해주세요
            }
            WateringState.WATERED_TODAY -> {
                // 오늘 물을 줬어요
            }
            WateringState.POSSIBLE -> {
                viewModelScope.launch {
                    wateringCropsUseCase(route.cropsId)
                        .onSuccess {
                            // ${crops.nickName}에 물을 줬어요
                        }
                        .onFailure {
                            // 물 주기에 실패했어요
                        }
                }
            }
        }
    }

    fun onHarvest() {
        viewModelScope.launch {
            harvestCropsUseCase(route.cropsId)
                .onSuccess {
                    showHarvestDialog = false
                    // ${crops.nickName}을/를 수확했어요
                }
                .onFailure {
                    // 수확을 실패했어요
                }
        }
    }

    fun onDelete(moveMain: () -> Unit) {
        viewModelScope.launch {
            deleteCropsUseCase(route.cropsId)
                .onSuccess {
                    showDeleteDialog = false
                    moveMain()
                    // ${crops.nickName}을/를 삭제했어요
                }
                .onFailure {
                    // 삭제에 실패했어요
                }
        }
    }
}