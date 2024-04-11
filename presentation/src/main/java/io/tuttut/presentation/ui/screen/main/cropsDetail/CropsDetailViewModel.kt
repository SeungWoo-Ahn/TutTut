package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropsDetailViewModel @Inject constructor(
    private val cropsRepo: CropsRepository,
    private val diaryRepo: DiaryRepository,
    cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel,
    private val prefs: PreferenceUtil
): BaseViewModel() {
    private val crops = cropsModel.observedCrops.value
    val cropsInfoMap = cropsInfoRepo.cropsInfoMap

    val uiState: StateFlow<CropsDetailUiState>
        = cropsRepo.getCropsDetail(
            gardenId = prefs.gardenId,
            cropsId = crops.id
        ).map(CropsDetailUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CropsDetailUiState.Loading
        )

    val diaryUiState: StateFlow<CropsDiaryUiState>
        = diaryRepo.getFourDiaryList(
            gardenId = prefs.gardenId,
            cropsId = crops.id
        ).map(CropsDiaryUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CropsDiaryUiState.Loading
        )

    val recipeUiState: StateFlow<CropsRecipeUiState>
        = cropsInfoRepo
            .getCropsRecipes(crops.name)
            .map(CropsRecipeUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CropsRecipeUiState.Loading
            )

    var showDeleteDialog by mutableStateOf(false)
    var showHarvestDialog by mutableStateOf(false)

    fun onMoveCropsInfo(cropsKey: String, moveCropsInfo: () -> Unit) {
        cropsModel.selectCropsInfo(cropsInfoMap[cropsKey] ?: CropsInfo(), true)
        moveCropsInfo()
    }

    fun onRecipe(link: String, moveRecipeWeb: () -> Unit) {
        cropsModel.setRecipeLink(link)
        moveRecipeWeb()
    }

    fun onDiary() {

    }


    fun moveAddDiary() {

    }

    fun onEdit(crops: Crops, moveEditCrops: () -> Unit) {
        cropsModel.selectCropsState(crops, true)
        moveEditCrops()
    }

    fun onHarvest(crops: Crops, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            cropsRepo.harvestCrops(prefs.gardenId, crops.id, crops.harvestCnt).collect {
                when (it) {
                    is Result.Success -> {
                        cropsModel.refreshHarvestedCropsList.value = true
                        cropsModel.refreshCropsList.value = true
                        showHarvestDialog = false
                        onShowSnackBar("${crops.nickName}을/를 수확했어요", null)
                    }
                    Result.Loading -> {}
                    else -> { TODO("에러 핸들링") }
                }
            }
        }
    }

    fun onWatering(crops: Crops, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            if (crops.wateringInterval == null) {
                onShowSnackBar("물 주기 간격을 설정해주세요", null)
            }
            else if (crops.lastWatered == getToday()) {
                onShowSnackBar("오늘 물을 줬어요", null)
            } else {
                cropsRepo.wateringCrops(
                    gardenId = prefs.gardenId,
                    cropsId = crops.id,
                    today = getToday()
                ).collect {
                    when (it) {
                        is Result.Success -> {
                            cropsModel.refreshCropsList(crops)
                            onShowSnackBar("${crops.nickName}에 물을 줬어요", null)
                        }
                        Result.Loading -> {}
                        else -> { TODO("에러 핸들링") }
                    }
                }
            }
        }
    }

    fun onDelete(crops: Crops, moveMain: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            cropsRepo.deleteCrops(prefs.gardenId, crops.id).collect {
                when (it) {
                    is Result.Success -> {
                        showDeleteDialog = false
                        cropsModel.refreshCropsList(crops)
                        moveMain()
                        onShowSnackBar("${crops.nickName}을/를 삭제했어요", null)
                    }
                    Result.Loading -> {}
                    else -> { TODO("에러 핸들링") }
                }
            }
        }
    }
}